
package com.fiap.alegorflix.movie.service;

import com.fiap.alegorflix.metrics.service.MetricService;
import com.fiap.alegorflix.movie.entity.Movie;
import com.fiap.alegorflix.movie.repository.MovieRepository;
import com.fiap.alegorflix.user.service.UserService;
import com.fiap.alegorflix.utils.MovieHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    private MovieService movieService;

    @Mock
    private MovieRepository repository;

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        movieService = new MovieService(repository, reactiveMongoTemplate);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class findMovies {

        @Test
        void shouldfindAllMovies() {

            Flux<Movie> flux = Flux.just(MovieHelper.createMovie());

            when(repository.findAllBy(any()))
                .thenReturn(flux);

            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("publishedDate"));

            var movies = movieService.findAll(pageRequest);

            assertThat(movies.block()).hasSize(1);
            assertThat(movies.block().getContent())
                .asList()
                .allSatisfy(movie -> {
                    assertThat(movie).isNotNull();
                    assertThat(movie).isInstanceOf(Movie.class);
                });

            verify(repository, times(1)).findAllBy(pageRequest);

        }

        @Test
        void shouldfindById() {

            Mono<Movie> mono = Mono.just(MovieHelper.createMovie());

            when(repository.findById(anyString()))
                .thenReturn(mono);

            var movie = movieService.findById(mono.block().getId());

            verify(repository, times(1))
                .findById(mono.block().getId());
            assertThat(movie.block().getId())
                .isEqualTo(mono.block().getId());

        }

        @Test
        void shouldfindByTitle() {

            Mono<Movie> mono = Mono.just(MovieHelper.createMovie());

            when(repository.findByTitle(anyString()))
                .thenReturn(mono);

            var movie = movieService.findByTitle(mono.block().getTitle());

            verify(repository, times(1))
                .findByTitle(mono.block().getTitle());
            assertThat(movie.block().getTitle())
                .isEqualTo(mono.block().getTitle());

        }
    }
}
