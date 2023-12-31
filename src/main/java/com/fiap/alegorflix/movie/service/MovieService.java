package com.fiap.alegorflix.movie.service;

import static com.fiap.alegorflix.utils.HandleNotFound.handleNotFound;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fiap.alegorflix.movie.controller.dto.MovieDto;
import com.fiap.alegorflix.movie.entity.Movie;
import com.fiap.alegorflix.movie.repository.MovieRepository;
import com.fiap.alegorflix.utils.DefaultService;

import lombok.RequiredArgsConstructor;
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
        return handleNotFound(repository.findById(movieId), movieId);
    }

    public Mono<Movie> create(MovieDto movie) {
        return repository.save(new Movie(movie));
    }

    public Mono<Movie> update(String id, MovieDto movie) {
        return findById(id)
                .map(movie::getMovieUpdated)
                .flatMap(repository::save);
    }

    public Mono<Void> deleteById(String id) {
        var movie = findById(id);
        return repository.delete(movie.block());
    }

}
