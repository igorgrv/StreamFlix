package com.fiap.alegorflix.movie.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.alegorflix.movie.controller.dto.MovieDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.fiap.alegorflix.exception.CustomExceptionHandler;
import com.fiap.alegorflix.exception.NotFoundException;
import com.fiap.alegorflix.movie.entity.Movie;
import com.fiap.alegorflix.movie.service.MovieService;
import com.fiap.alegorflix.utils.MovieHelper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fiap.alegorflix.exception.CustomExceptionHandler;
import com.fiap.alegorflix.movie.entity.Movie;
import com.fiap.alegorflix.movie.service.MovieService;
import com.fiap.alegorflix.utils.MovieHelper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MovieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MovieService service;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        MovieController movieController = new MovieController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(movieController)
            .setControllerAdvice(new CustomExceptionHandler())
            .addFilter((request, response, chain) -> {
                response.setCharacterEncoding("UTF-8");
                chain.doFilter(request, response);
            }, "/*")
            .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class FindAllMovies {

        @Test
        void shouldAllowFindAll() throws Exception {
            var movie = MovieHelper.createMovie();
            Page<Movie> page = new PageImpl<>(Collections.singletonList(
                movie
            ));
            Mono mono = Mono.just(page);
            when(service.findAll(any(Pageable.class)))
                .thenReturn(mono);

            mockMvc.perform(get("/movies")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncResult(page))
                .andExpect(status().isOk());

            verify(service, times(1))
                .findAll(any(Pageable.class));
        }

        @Test
        void shouldAllowFindAllWhenEmptyList() throws Exception {
            Page<Movie> page = new PageImpl<>(Collections.emptyList());
            Mono mono = Mono.just(page);
            when(service.findAll(any(Pageable.class)))
                .thenReturn(mono);

            mockMvc.perform(get("/movies")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncResult(new PageImpl<>(Collections.emptyList())))
                .andExpect(status().isOk());

            verify(service, times(1))
                .findAll(any(Pageable.class));
        }

        @Test
        void shouldAllowFindAllWhenReceiveWrongParameters() throws Exception {
            Page<Movie> page = new PageImpl<>(Collections.emptyList());
            Mono mono = Mono.just(page);
            when(service.findAll(any(Pageable.class)))
                .thenReturn(mono);

            mockMvc.perform(get("/movies?page=2&ping=pong")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncResult(new PageImpl<>(Collections.emptyList())))
                .andExpect(status().isOk());

            verify(service, times(1))
                .findAll(any(Pageable.class));
        }
    }

    @Nested
    class FindMovie {

        @Test
        void shouldAllowFindMovieByID() throws Exception {
            var movie = MovieHelper.createMovie();

            Mono mono = Mono.just(movie);
            when(service.findById(any(String.class)))
                .thenReturn(mono);

            MvcResult mvcResult = mockMvc.perform(get("/movies/{id}", movie.getId()))
                .andExpect(request().asyncStarted())
                .andReturn();

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(movie.getId()))
                .andExpect(jsonPath("$.title").value(movie.getTitle()))
                .andExpect(jsonPath("$.description").value(movie.getDescription()))
                .andExpect(jsonPath("$.category").value(movie.getCategory()))
                .andExpect(jsonPath("$.url").value(movie.getUrl()))
                .andExpect(jsonPath("$.views").value(movie.getViews()))
                .andExpect(jsonPath("$.version").value(movie.getVersion()));

            verify(service, times(1))
                .findById(any(String.class));
        }

        @Test
        void shouldGenerateExceptionWhenIDNotFound() throws Exception {
            var movie = MovieHelper.createMovie();

            when(service.findById(any(String.class)))
                .thenReturn(Mono.error(new NotFoundException("Id not found")));

            MvcResult mvcResult = mockMvc.perform(get("/movies/{id}", movie.getId()))
                .andExpect(request().asyncStarted())
                .andReturn();

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

            verify(service, times(1))
                .findById(any(String.class));
        }

        @Test
        void shouldAllowFindMovieByTitlePublishedDate() throws Exception {
            var movie = MovieHelper.createMovie();

            Mono mono = Mono.just(movie);
            when(service.findByFilters(any(Map.class), any(PageRequest.class)))
                .thenReturn(mono);

            MvcResult mvcResult = mockMvc.perform(get("/movies/search")
                    .param("title", "Scream")
                    .param("publishedDate", String.valueOf(LocalDate.now()))
                    .param("category", "HORROR"))
                .andExpect(request().asyncStarted())
                .andReturn();

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(movie.getId()))
                .andExpect(jsonPath("$.title").value(movie.getTitle()))
                .andExpect(jsonPath("$.description").value(movie.getDescription()))
                .andExpect(jsonPath("$.category").value(movie.getCategory()))
                .andExpect(jsonPath("$.url").value(movie.getUrl()))
                .andExpect(jsonPath("$.views").value(movie.getViews()))
                .andExpect(jsonPath("$.version").value(movie.getVersion()));

            verify(service, times(1))
                .findByFilters(any(Map.class), any(PageRequest.class));
        }

        @Test
        void shouldAllowFindMovieByListOfCategories() throws Exception {
            var movie = MovieHelper.createMovie();
            Set<String> categories = Set.of("Horror");

            Flux flux = Flux.just(movie);
            when(service.findByCategories(anySet()))
                .thenReturn(flux);

            MvcResult mvcResult = mockMvc.perform(post("/movies/categories")
                .content(asJsonString(categories))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

                mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

            verify(service, times(1))
                .findByCategories(anySet());
        }
    }

    @Nested
    class CreateMovie {

        @Test
        void shouldAllowCreateNewMovie() throws Exception {
            var movieDTO = MovieHelper.createMovieDTO();
            var movie = MovieHelper.createMovie();

            Mono mono = Mono.just(movie);
            when(service.create(any(MovieDto.class)))
                .thenReturn(mono);

            MvcResult mvcResult = mockMvc.perform(post("/movies")
                    .content(asJsonString(movieDTO))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

            verify(service, times(1))
                .create(any(MovieDto.class));
        }
    }

    @Nested
    class UpdateMovie {

        @Test
        void shouldAllowUpdateMovie() throws Exception {
            var movieDTO = MovieHelper.createMovieDTO();
            var movie = MovieHelper.createMovie();

            Mono mono = Mono.just(movie);
            when(service.update(anyString() ,any(MovieDto.class)))
                .thenReturn(mono);

            MvcResult mvcResult = mockMvc.perform(put("/movies/{id}", 1)
                    .content(asJsonString(movieDTO))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

            verify(service, times(1))
                .update(anyString() ,any(MovieDto.class));
        }
    }
    @Nested
    class DeleteMovie {

        @Test
        void shouldAllowDeleteMovie() throws Exception {
            Mono mono = Mono.just("Movie " + 1 + " deleted with success");

            when(service.deleteById(anyString()))
                .thenReturn(mono);

            MvcResult mvcResult = mockMvc.perform(delete("/movies/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

            mockMvc.perform(asyncDispatch(mvcResult));

            verify(service, times(1))
                .deleteById(anyString());
        }
    }

    @Nested
    class WatchMovie {

        @Test
        void shouldAllowWatchMovie() throws Exception {
            var movie = MovieHelper.createMovie();

            Mono mono = Mono.just(movie);
            when(service.watchMovie(anyString()))
                .thenReturn(mono);

            MvcResult mvcResult = mockMvc.perform(post("/movies/watch/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

            verify(service, times(1))
                .watchMovie(anyString());
        }
    }



    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
