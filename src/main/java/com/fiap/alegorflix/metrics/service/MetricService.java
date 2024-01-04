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
        Mono<Long> numberFavoriteMovies = userService.getNumberFavoriteMovies();

        Mono<Long> numberOfMovies = movieService.getNumberOfMovies();
        Mono<Long> countViewsOfAllMovies = movieService.countViewsOfAllMovies();
        Mono<Long> averageWatched = getAverage(numberOfMovies, countViewsOfAllMovies);

        return Mono.zip(numberOfMovies, numberFavoriteMovies, averageWatched)
                .map(tuple -> new Metric(tuple.getT1().intValue(), tuple.getT2().intValue(), tuple.getT3().intValue()));
    }

    public Mono<Long> getAverage(Mono<Long> numberOfMovies, Mono<Long> countViewsOfAllMovies) {
        return Mono.zip(numberOfMovies, countViewsOfAllMovies)
                .map(tuple -> {
                    long totalMovies = tuple.getT1();
                    long totalViews = tuple.getT2();

                    double average = totalMovies > 0 ? (double) totalViews / totalMovies : 0;
                    return Math.round(average);
                });
    }

}
