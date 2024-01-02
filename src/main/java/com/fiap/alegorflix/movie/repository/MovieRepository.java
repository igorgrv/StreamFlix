package com.fiap.alegorflix.movie.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.fiap.alegorflix.movie.entity.Movie;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieRepository extends ReactiveMongoRepository<Movie, String> {
    Flux<Movie> findAllBy(Pageable pageable);

    Mono<Movie> findByTitle(String title);

    Flux<Movie> findByPublishedDate(LocalDate publishedDate);

    Flux<Movie> findByTitleAndPublishedDate(String title, LocalDate publishedDate);

    Mono<Boolean> existsByTitle(String title);
}
