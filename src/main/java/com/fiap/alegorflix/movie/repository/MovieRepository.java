package com.fiap.alegorflix.movie.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.fiap.alegorflix.movie.entity.Movie;

import reactor.core.publisher.Flux;

public interface MovieRepository extends ReactiveMongoRepository<Movie, String> {
    Flux<Movie> findAllBy(Pageable pageable);
}