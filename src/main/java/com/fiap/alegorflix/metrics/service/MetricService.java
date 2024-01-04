package com.fiap.alegorflix.metrics.service;

import org.springframework.stereotype.Service;

import com.fiap.alegorflix.metrics.entity.Metric;
import com.fiap.alegorflix.movie.service.MovieService;
import com.fiap.alegorflix.user.service.UserService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MetricService {

    private final MovieService movieService;
    private final UserService userService;

    public Mono<Metric> getMetrics() {
        Mono<Long> numberOfMovies = movieService.getNumberOfMovies();
        Mono<Long> numberFavoriteMovies = userService.getNumberFavoriteMovies();

        return Mono.zip(numberOfMovies, numberFavoriteMovies)
                .map(tuple -> new Metric(tuple.getT1().intValue(), tuple.getT2().intValue()));
    }

}
