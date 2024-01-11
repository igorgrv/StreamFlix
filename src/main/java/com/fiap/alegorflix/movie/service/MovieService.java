package com.fiap.alegorflix.movie.service;

import static com.fiap.alegorflix.utils.HandleNotFound.handleNotFound;
import static com.fiap.alegorflix.utils.HandleNotFound.handleNotFoundFlux;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fiap.alegorflix.exception.AlreadyExistsException;
import com.fiap.alegorflix.movie.controller.dto.MovieDto;
import com.fiap.alegorflix.movie.entity.CategoryEnum;
import com.fiap.alegorflix.movie.entity.Movie;
import com.fiap.alegorflix.movie.repository.MovieRepository;
import com.fiap.alegorflix.utils.DefaultService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MovieService implements DefaultService<Movie, MovieDto> {

    private final MovieRepository repository;

    public Mono<PageImpl<Movie>> findAll(Pageable pageable) {
        return repository.findAllBy(pageable)
                .collectList()
                .map(movies -> new PageImpl<>(movies, pageable, movies.size()));
    }

    public Mono<Movie> findById(String movieId) {
        return handleNotFound(repository.findById(movieId), Movie.class, movieId);
    }

    public Mono<Movie> findByTitle(String movieTitle) {
        return handleNotFound(repository.findByTitle(movieTitle), Movie.class, movieTitle);
    }

    public Flux<Movie> findByPublishedDate(LocalDate publishedDate) {
        return handleNotFoundFlux(repository.findByPublishedDate(publishedDate), Movie.class, publishedDate);
    }

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<PageImpl<Movie>> findByFilters(Map<String, Object> filters, PageRequest pageRequest) {

        if (filters.values().stream().allMatch(Objects::isNull))
            return findAll(pageRequest);

        Query query = new Query();

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            String filter = entry.getKey();
            Object value = entry.getValue();

            if (filter.equalsIgnoreCase("category"))
                value = CategoryEnum.fromString(value.toString()).getCategory();

            if (value != null)
                query.addCriteria(Criteria.where(filter).is(value));

        }

        return reactiveMongoTemplate.find(query, Movie.class)
                .collectList()
                .map(movies -> new PageImpl<>(movies, pageRequest, movies.size()));
    }

    private Mono<Boolean> existByTitle(String title) {
        return repository.existsByTitle(title);
    }

    public Mono<Movie> create(MovieDto movie) {
        return existByTitle(movie.title()).flatMap(titleExists -> {
            if (Boolean.TRUE.equals(titleExists))
                return Mono
                        .error(new AlreadyExistsException("Movie with title '" + movie.title() + "' already exists"));
            return repository.save(new Movie(movie));
        });
    }

    public Mono<Movie> update(String id, MovieDto movie) {
        return findById(id)
                .map(movie::getMovieUpdated)
                .flatMap(repository::save);
    }

    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id).then();
    }

    public Flux<Movie> findByCategories(Set<String> movieCategories) {
        return repository.findByCategories(movieCategories);
    }

    public Mono<Long> getNumberOfMovies() {
        return repository.count();
    }

    public Mono<Movie> watchMovie(String id) {
        return findById(id).flatMap(movie -> {
            Integer views = movie.getViews();
            if (views == null)
                movie.setViews(0);
            views++;
            movie.setViews(views);
            return repository.save(movie);
        });
    }

    public Mono<Long> countViewsOfAllMovies() {
        return repository.findAll()
                .map(Movie::getViews)
                .reduce(0, Integer::sum)
                .map(Long::valueOf);
    }

}
