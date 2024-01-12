package com.fiap.alegorflix.movie.controller;

import com.fiap.alegorflix.exception.CustomExceptionHandler;
import com.fiap.alegorflix.movie.entity.Movie;
import com.fiap.alegorflix.movie.service.MovieService;
import com.fiap.alegorflix.utils.MovieHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.content[0].title").value(movie.getTitle()))
//                .andExpect(jsonPath("$.content[0].description").value(movie.getDescription()))
//                .andExpect(jsonPath("$.content[0].category").value(movie.getCategory()))
//                .andExpect(jsonPath("$.content[0].url").value(movie.getUrl()))
//                .andExpect(jsonPath("$.content[0].publishedDate").value(movie.getDescription()));
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
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.content").isArray())
//                .andExpect(jsonPath("$.content", empty()))
//                .andExpect(jsonPath("$.content", hasSize(0)));
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
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.content").isArray())
//                .andExpect(jsonPath("$.content", empty()))
//                .andExpect(jsonPath("$.content", hasSize(0)));
            verify(service, times(1))
                .findAll(any(Pageable.class));
        }
    }

    @Nested
    class FindMovie {

        @Test
        void shouldAllowFindMovie() throws Exception {
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
    }
}
