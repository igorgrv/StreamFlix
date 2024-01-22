
package com.fiap.alegorflix.movie.service;

import com.fiap.alegorflix.exception.AlreadyExistsException;
import com.fiap.alegorflix.exception.NotFoundException;
import com.fiap.alegorflix.movie.entity.Movie;
import com.fiap.alegorflix.movie.repository.MovieRepository;
import com.fiap.alegorflix.utils.MovieHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        void shouldFindAllMovies() {

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
        void shouldFindById() {

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
        void shouldFindByTitle() {

            Mono<Movie> mono = Mono.just(MovieHelper.createMovie());

            when(repository.findByTitle(anyString()))
                .thenReturn(mono);

            var movie = movieService.findByTitle(mono.block().getTitle());

            verify(repository, times(1))
                .findByTitle(mono.block().getTitle());
            assertThat(movie.block().getTitle())
                .isEqualTo(mono.block().getTitle());

        }

        @Test
        void shouldFindByPublishedDate() {

            Flux<Movie> flux = Flux.just(MovieHelper.createMovie());

            when(repository.findByPublishedDate(any(LocalDate.class)))
                .thenReturn(flux);

            var movie = movieService.findByPublishedDate(flux.blockFirst().getPublishedDate());

            verify(repository, times(1))
                .findByPublishedDate(flux.blockFirst().getPublishedDate());
            assertThat(movie.blockFirst().getTitle())
                .isEqualTo(movie.blockFirst().getTitle());

        }

        @Test
        void shouldFindByPublishedDateNotFoundException() {

            Flux<Movie> flux = Flux.just(MovieHelper.createMovie());

            when(repository.findByPublishedDate(any(LocalDate.class)))
                .thenReturn(Flux.empty());

            var movie = movieService.findByPublishedDate(flux.blockFirst().getPublishedDate());

            assertThatThrownBy(() -> movie.blockFirst())
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Could not find any Movie given: " + flux.blockFirst().getPublishedDate());
        }

        @Test
        void shouldFindByFilters() {

            Map<String, Object> filters = new HashMap<>();
            filters.put("title", MovieHelper.createMovie().getTitle());
            filters.put("publishedDate", MovieHelper.createMovie().getPublishedDate());
            filters.put("category", MovieHelper.createMovie().getCategory());

            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("publishedDate"));

            when(reactiveMongoTemplate.find(any(Query.class), any()))
                .thenReturn(Flux.just(MovieHelper.createMovie()));

            var movie = movieService.findByFilters(filters, pageRequest);

            verify(reactiveMongoTemplate, times(1))
                .find(any(Query.class), any());

            assertThat(movie.block().getContent().get(0).getTitle())
                .isEqualTo(MovieHelper.createMovie().getTitle());
        }

        @Test
        void shouldFindByCategories() {
            Set<String> categories = Set.of("Horror");

            Flux<Movie> flux = Flux.just(MovieHelper.createMovie());

            when(repository.findByCategories(anySet()))
                .thenReturn(flux);

            var movie = movieService.findByCategories(categories);

            verify(repository, times(1))
                .findByCategories(categories);
            assertThat(movie.blockFirst().getCategory())
                .isEqualTo(categories.iterator().next());
        }

        @Test
        void shouldGetNumberOfMovies() {
            Mono<Long> mono = Mono.just(1L);

            when(repository.count())
                .thenReturn(mono);

            var movie = movieService.getNumberOfMovies();

            verify(repository, times(1))
                .count();
            assertThat(movie.block().longValue())
                .isEqualTo(mono.block().longValue());
        }

        @Test
        void shouldCountViewsOfAllMovies() {
            Mono<Long> mono = Mono.just(1000L);
            Flux<Movie> flux = Flux.just(MovieHelper.createMovie());

            when(repository.findAll())
                .thenReturn(flux);

            var movie = movieService.countViewsOfAllMovies();

            verify(repository, times(1))
                .findAll();
            assertThat(movie.block().longValue())
                .isEqualTo(mono.block().longValue());
        }

    }

    @Nested
    class createMovie {

        @Test
        void shouldCreateMovie() {

            var movie = MovieHelper.createMovieDTO();
            when(repository.save(any(Movie.class)))
                .thenReturn(Mono.just(MovieHelper.createMovie()));
            when(repository.existsByTitle(anyString()))
                .thenReturn(Mono.just(Boolean.FALSE));

            var movieCreated = movieService.create(movie);

            assertThat(movieCreated)
                .isNotNull();
            assertThat(movieCreated.block().getTitle())
                .isEqualTo(movie.title());
            verify(repository, times(1)).save(any(Movie.class));
        }

        @Test
        void shouldCreateMovieAlreadyExistsException() {

            var movie = MovieHelper.createMovieDTO();
            when(repository.save(any(Movie.class)))
                .thenReturn(Mono.just(MovieHelper.createMovie()));

            when(repository.existsByTitle(anyString()))
                .thenReturn(Mono.just(Boolean.TRUE));

            var movieCreated = movieService.create(movie);

            assertThatThrownBy(() -> movieCreated.block())
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessage("Movie with title '" + movie.title() + "' already exists");

        }


        @Test
        void shouldWatchMovie() {

            var movie = MovieHelper.createMovie();
            movie.setViews(1001);

            when(repository.findById(anyString()))
                .thenReturn(Mono.just(MovieHelper.createMovie()));

            when(repository.save(any(Movie.class)))
                .thenReturn(Mono.just(movie));

            var movieWatched = movieService.watchMovie(movie.getId());

            assertThat(movieWatched)
                .isNotNull();
            assertThat(movieWatched.block().getViews())
                .isEqualTo(1001);
            verify(repository, times(1)).save(any(Movie.class));
            verify(repository, times(1)).findById(anyString());
        }
    }

    @Nested
    class updateMovie {

        @Test
        void shouldUpdateMovie() {

            var movieDTO = MovieHelper.createMovieDTO();
            var movie = MovieHelper.createMovie();
            when(repository.findById(anyString()))
                .thenReturn(Mono.just(movie));
            when(repository.save(any(Movie.class)))
                .thenReturn(Mono.just(movie));

            var movieUpdated = movieService.update(movie.getId(), movieDTO);

            assertThat(movieUpdated)
                .isNotNull();
            assertThat(movieUpdated.block().getTitle())
                .isEqualTo(movieDTO.title());
            verify(repository, times(1)).save(any(Movie.class));
        }
    }

    @Nested
    class deleteMovie {

        @Test
        void shouldDeleteMovie() {

            when(repository.deleteById(anyString()))
                .thenReturn(Mono.empty());

            movieService.deleteById("1");

            verify(repository, times(1)).deleteById(anyString());
        }
    }
}
