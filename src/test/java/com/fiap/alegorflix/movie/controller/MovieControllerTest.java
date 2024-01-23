package com.fiap.alegorflix.movie.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fiap.alegorflix.movie.entity.Movie;
import com.fiap.alegorflix.movie.repository.MovieRepository;
import com.fiap.alegorflix.movie.service.MovieService;
import com.fiap.alegorflix.utils.MovieHelper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MovieControllerTest {

    @InjectMocks
    private MovieService service;

    @Mock
    private MovieRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class FindAllMovies {

        @Test
        void shouldAllowFindAll() throws Exception {
            List<Movie> listMovies = MovieHelper.createListMovies();
            when(repository.findAllBy(any(Pageable.class))).thenReturn(Flux.fromIterable(listMovies));

            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("publishedDate"));
            Mono<PageImpl<Movie>> pageOfMovies = MovieHelper.createPageOfMovies(listMovies, pageRequest);

            Mono<PageImpl<Movie>> movies = service.findAll(pageRequest);
            
            assertEquals(movies.block(), pageOfMovies.block());
        }

        @Test
        void shouldAllowFindAllWhenEmptyList() throws Exception {
            List<Movie> emptyList = Collections.emptyList();
            when(repository.findAllBy(any(Pageable.class))).thenReturn(Flux.fromIterable(emptyList));

            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("publishedDate"));
            Mono<PageImpl<Movie>> pageOfMovies = MovieHelper.createPageOfMovies(emptyList, pageRequest);

            Mono<PageImpl<Movie>> movies = service.findAll(pageRequest);
            
            assertEquals(movies.block(), pageOfMovies.block());
        }
    }

    // @Nested
    // class FindMovie {

    //     @Test
    //     void shouldAllowFindMovieByID() throws Exception {
    //         var movie = MovieHelper.createMovie();

    //         Mono mono = Mono.just(movie);
    //         when(service.findById(any(String.class)))
    //             .thenReturn(mono);

    //         MvcResult mvcResult = mockMvc.perform(get("/movies/{id}", movie.getId()))
    //             .andExpect(request().asyncStarted())
    //             .andReturn();

    //         mockMvc.perform(asyncDispatch(mvcResult))
    //             .andExpect(status().isOk())
    //             .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    //             .andExpect(jsonPath("$.id").value(movie.getId()))
    //             .andExpect(jsonPath("$.title").value(movie.getTitle()))
    //             .andExpect(jsonPath("$.description").value(movie.getDescription()))
    //             .andExpect(jsonPath("$.category").value(movie.getCategory()))
    //             .andExpect(jsonPath("$.url").value(movie.getUrl()))
    //             .andExpect(jsonPath("$.views").value(movie.getViews()))
    //             .andExpect(jsonPath("$.version").value(movie.getVersion()));

    //         verify(service, times(1))
    //             .findById(any(String.class));
    //     }

    //     @Test
    //     void shouldGenerateExceptionWhenIDNotFound() throws Exception {
    //         var movie = MovieHelper.createMovie();

    //         when(service.findById(any(String.class)))
    //             .thenReturn(Mono.error(new NotFoundException("Id not found")));

    //         MvcResult mvcResult = mockMvc.perform(get("/movies/{id}", movie.getId()))
    //             .andExpect(request().asyncStarted())
    //             .andReturn();

    //         mockMvc.perform(asyncDispatch(mvcResult))
    //             .andExpect(status().isNotFound())
    //             .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

    //         verify(service, times(1))
    //             .findById(any(String.class));
    //     }

    //     @Test
    //     void shouldAllowFindMovieByTitlePublishedDate() throws Exception {
    //         var movie = MovieHelper.createMovie();

    //         Mono mono = Mono.just(movie);
    //         when(service.findByFilters(any(Map.class), any(PageRequest.class)))
    //             .thenReturn(mono);

    //         MvcResult mvcResult = mockMvc.perform(get("/movies/search")
    //                 .param("title", "Scream")
    //                 .param("publishedDate", String.valueOf(LocalDate.now()))
    //                 .param("category", "HORROR"))
    //             .andExpect(request().asyncStarted())
    //             .andReturn();

    //         mockMvc.perform(asyncDispatch(mvcResult))
    //             .andExpect(status().isOk())
    //             .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    //             .andExpect(jsonPath("$.id").value(movie.getId()))
    //             .andExpect(jsonPath("$.title").value(movie.getTitle()))
    //             .andExpect(jsonPath("$.description").value(movie.getDescription()))
    //             .andExpect(jsonPath("$.category").value(movie.getCategory()))
    //             .andExpect(jsonPath("$.url").value(movie.getUrl()))
    //             .andExpect(jsonPath("$.views").value(movie.getViews()))
    //             .andExpect(jsonPath("$.version").value(movie.getVersion()));

    //         verify(service, times(1))
    //             .findByFilters(any(Map.class), any(PageRequest.class));
    //     }

    //     @Test
    //     void shouldAllowFindMovieByListOfCategories() throws Exception {
    //         var movie = MovieHelper.createMovie();
    //         Set<String> categories = Set.of("Horror");

    //         Flux flux = Flux.just(movie);
    //         when(service.findByCategories(anySet()))
    //             .thenReturn(flux);

    //         MvcResult mvcResult = mockMvc.perform(post("/movies/categories")
    //             .content(asJsonString(categories))
    //             .contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(request().asyncStarted())
    //             .andReturn();

    //             mockMvc.perform(asyncDispatch(mvcResult))
    //             .andExpect(status().isOk())
    //             .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    //         verify(service, times(1))
    //             .findByCategories(anySet());
    //     }
    // }

    // @Nested
    // class CreateMovie {

    //     @Test
    //     void shouldAllowCreateNewMovie() throws Exception {
    //         var movieDTO = MovieHelper.createMovieDTO();
    //         var movie = MovieHelper.createMovie();

    //         Mono mono = Mono.just(movie);
    //         when(service.create(any(MovieDto.class)))
    //             .thenReturn(mono);

    //         MvcResult mvcResult = mockMvc.perform(post("/movies")
    //                 .content(asJsonString(movieDTO))
    //                 .contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(request().asyncStarted())
    //             .andReturn();

    //         mockMvc.perform(asyncDispatch(mvcResult))
    //             .andExpect(status().isCreated())
    //             .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    //         verify(service, times(1))
    //             .create(any(MovieDto.class));
    //     }
    // }

    // @Nested
    // class UpdateMovie {

    //     @Test
    //     void shouldAllowUpdateMovie() throws Exception {
    //         var movieDTO = MovieHelper.createMovieDTO();
    //         var movie = MovieHelper.createMovie();

    //         Mono mono = Mono.just(movie);
    //         when(service.update(anyString() ,any(MovieDto.class)))
    //             .thenReturn(mono);

    //         MvcResult mvcResult = mockMvc.perform(put("/movies/{id}", 1)
    //                 .content(asJsonString(movieDTO))
    //                 .contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(request().asyncStarted())
    //             .andReturn();

    //         mockMvc.perform(asyncDispatch(mvcResult))
    //             .andExpect(status().isOk())
    //             .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    //         verify(service, times(1))
    //             .update(anyString() ,any(MovieDto.class));
    //     }
    // }
    // @Nested
    // class DeleteMovie {

    //     @Test
    //     void shouldAllowDeleteMovie() throws Exception {
    //         Mono mono = Mono.just("Movie " + 1 + " deleted with success");

    //         when(service.deleteById(anyString()))
    //             .thenReturn(mono);

    //         MvcResult mvcResult = mockMvc.perform(delete("/movies/{id}", 1)
    //                 .contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(request().asyncStarted())
    //             .andReturn();

    //         mockMvc.perform(asyncDispatch(mvcResult));

    //         verify(service, times(1))
    //             .deleteById(anyString());
    //     }
    // }

    // @Nested
    // class WatchMovie {

    //     @Test
    //     void shouldAllowWatchMovie() throws Exception {
    //         var movie = MovieHelper.createMovie();

    //         Mono mono = Mono.just(movie);
    //         when(service.watchMovie(anyString()))
    //             .thenReturn(mono);

    //         MvcResult mvcResult = mockMvc.perform(post("/movies/watch/{id}", 1)
    //                 .contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(request().asyncStarted())
    //             .andReturn();

    //         mockMvc.perform(asyncDispatch(mvcResult))
    //             .andExpect(status().isOk())
    //             .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    //         verify(service, times(1))
    //             .watchMovie(anyString());
    //     }
    // }



    // public static String asJsonString(final Object obj) {
    //     try {
    //         return new ObjectMapper().writeValueAsString(obj);
    //     } catch (Exception e) {
    //         throw new RuntimeException(e);
    //     }
    // }
}
