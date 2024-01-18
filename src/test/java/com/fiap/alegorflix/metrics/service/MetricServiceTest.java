
package com.fiap.alegorflix.metrics.service;

import com.fiap.alegorflix.movie.service.MovieService;
import com.fiap.alegorflix.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

class MetricServiceTest {

    private MetricService metricService;

    @Mock
    private MovieService movieService;

    @Mock
    private UserService userService;
    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        metricService = new MetricService(movieService, userService);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }
    @Nested
    class GetMetric {

        @Test
        void shouldGetMetrics() {
            Mono<Long> numberFavoriteMovies = Mono.just(5L);
            Mono<Long> numberOfMovies = Mono.just(20L);
            Mono<Long> countViewsOfAllMovies = Mono.just(800L);

            when(userService.getNumberFavoriteMovies())
                .thenReturn(numberFavoriteMovies);
            when(movieService.getNumberOfMovies())
                .thenReturn(numberOfMovies);
            when(movieService.countViewsOfAllMovies())
                .thenReturn(countViewsOfAllMovies);

            var metrics = metricService.getMetrics();

            verify(userService, times(1))
                .getNumberFavoriteMovies();
            verify(movieService, times(1))
                .getNumberOfMovies();
            verify(movieService, times(1))
                .countViewsOfAllMovies();

            assertThat(metrics.block().avgViews())
                .isEqualTo(40);
        }
    }
}
