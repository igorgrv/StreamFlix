package com.fiap.alegorflix.user.service;

import static com.fiap.alegorflix.utils.HandleNotFound.handleNotFound;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fiap.alegorflix.movie.entity.Movie;
import com.fiap.alegorflix.movie.service.MovieService;
import com.fiap.alegorflix.user.controller.dto.UserDto;
import com.fiap.alegorflix.user.entity.User;
import com.fiap.alegorflix.user.repository.UserRepository;
import com.fiap.alegorflix.utils.DefaultService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService implements DefaultService<User, UserDto> {

    private final UserRepository repository;
    private final MovieService movieService;

    public Mono<PageImpl<User>> findAll(Pageable pageable) {
        return repository.findAllBy(pageable)
                .collectList()
                .map(users -> new PageImpl<>(users, pageable, users.size()));
    }

    public Mono<User> findById(String userId) {
        return handleNotFound(repository.findById(userId), User.class, userId);
    }

    public Mono<User> create(UserDto user) {
        return repository.save(new User(user));
    }

    public Mono<User> update(String id, UserDto user) {
        return findById(id)
                .map(user::getUserUpdated)
                .flatMap(repository::save);
    }

    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id).then();
    }

    public Mono<User> addFavoriteMovie(String id, String movieId) {

        Mono<User> userMono = findById(id);
        Mono<Movie> movieMono = movieService.findById(movieId);

        return userMono.flatMap(user -> movieMono.map(movie -> {

            if (user.getFavoriteMovies() == null)
                user.setFavoriteMovies(new HashSet<>());

            user.getFavoriteMovies().add(movie);
            return user;
        })).flatMap(repository::save);
    }

    public Flux<Movie> findRecommendedMoviesGiven(String userId) {
        return findById(userId).flatMapMany(user -> {
            Set<Movie> favoriteMovies = user.getFavoriteMovies();
            if (favoriteMovies == null || favoriteMovies.isEmpty())
                return Flux.empty();

            Set<String> movieCategories = favoriteMovies.stream().map(Movie::getCategory).collect(Collectors.toSet());
            return movieService.findByCategories(movieCategories);
        });
    }

    public Mono<Long> getNumberFavoriteMovies() {
        return repository.findAll()
                .flatMap(user -> Flux.fromIterable(user.getFavoriteMovies()))
                .count();
    }

}
