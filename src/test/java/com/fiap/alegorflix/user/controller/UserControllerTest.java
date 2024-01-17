package com.fiap.alegorflix.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.alegorflix.exception.CustomExceptionHandler;
import com.fiap.alegorflix.exception.NotFoundException;
import com.fiap.alegorflix.movie.controller.MovieController;
import com.fiap.alegorflix.movie.controller.dto.MovieDto;
import com.fiap.alegorflix.movie.entity.Movie;
import com.fiap.alegorflix.movie.service.MovieService;
import com.fiap.alegorflix.user.controller.dto.UserDto;
import com.fiap.alegorflix.user.entity.User;
import com.fiap.alegorflix.user.service.UserService;
import com.fiap.alegorflix.utils.MovieHelper;
import com.fiap.alegorflix.utils.UserHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService service;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        UserController userController = new UserController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
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
    class FindAllUsers {

        @Test
        void shouldAllowFindAll() throws Exception {
            var user = UserHelper.createUser();
            Page<User> page = new PageImpl<>(Collections.singletonList(
                user
            ));
            Mono mono = Mono.just(page);
            when(service.findAll(any(Pageable.class)))
                .thenReturn(mono);

            mockMvc.perform(get("/users")
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

            mockMvc.perform(get("/users")
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

            mockMvc.perform(get("/users?page=2&ping=pong")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncResult(new PageImpl<>(Collections.emptyList())))
                .andExpect(status().isOk());

            verify(service, times(1))
                .findAll(any(Pageable.class));
        }
    }

    @Nested
    class FindUser {

        @Test
        void shouldAllowFindUserByID() throws Exception {
            var user = UserHelper.createUser();

            Mono mono = Mono.just(user);
            when(service.findById(any(String.class)))
                .thenReturn(mono);

            MvcResult mvcResult = mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(request().asyncStarted())
                .andReturn();

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").value(user.getPassword()));

            verify(service, times(1))
                .findById(any(String.class));
        }

        @Test
        void shouldGenerateExceptionWhenIDNotFound() throws Exception {
            var user = UserHelper.createUser();

            when(service.findById(any(String.class)))
                .thenReturn(Mono.error(new NotFoundException("Id not found")));

            MvcResult mvcResult = mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(request().asyncStarted())
                .andReturn();

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

            verify(service, times(1))
                .findById(any(String.class));
        }
    }

    @Nested
    class CreateUser {

        @Test
        void shouldAllowCreateNewUser() throws Exception {
            var userDto = UserHelper.createUserDto();
            var user = UserHelper.createUser();

            Mono mono = Mono.just(user);
            when(service.create(any(UserDto.class)))
                .thenReturn(mono);

            MvcResult mvcResult = mockMvc.perform(post("/users")
                    .content(asJsonString(userDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

            verify(service, times(1))
                .create(any(UserDto.class));
        }

//        @Test
//        void shouldAllowAddFavorite() throws Exception {
//            var user = UserHelper.createUser();
//
//            Mono mono = Mono.just(user);
//            when(service.addFavoriteMovie(anyString(), anyString()))
//                .thenReturn(mono);
//
//            MvcResult mvcResult = mockMvc.perform(post("/users", user.getId(), 1)
//                    .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(request().asyncStarted())
//                .andReturn();
//
//            mockMvc.perform(asyncDispatch(mvcResult))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
//
//            verify(service, times(1))
//                .addFavoriteMovie(anyString(), anyString());
//        }
    }

    @Nested
    class UpdateUser {

        @Test
        void shouldAllowUpdateUser() throws Exception {
            var userDto = UserHelper.createUserDto();
            var user = UserHelper.createUser();

            Mono mono = Mono.just(user);
            when(service.update(anyString(), any(UserDto.class)))
                .thenReturn(mono);

            MvcResult mvcResult = mockMvc.perform(put("/users/{id}", 1)
                    .content(asJsonString(userDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

            verify(service, times(1))
                .update(anyString(), any(UserDto.class));
        }
    }

    @Nested
    class DeleteUser {

        @Test
        void shouldAllowDeleteUser() throws Exception {
            Mono mono = Mono.just("User " + 1 + " deleted with success");

            when(service.deleteById(anyString()))
                .thenReturn(mono);

            MvcResult mvcResult = mockMvc.perform(delete("/users/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

            mockMvc.perform(asyncDispatch(mvcResult));

            verify(service, times(1))
                .deleteById(anyString());
        }
    }

    @Nested
    class RecommendedMoviesGiven {

        @Test
        void shouldAllowRecommendedMoviesGiven() throws Exception {
            var movie = MovieHelper.createMovie();
            var user = UserHelper.createUser();

            Flux flux = Flux.just(movie);
            when(service.findRecommendedMoviesGiven(anyString()))
                .thenReturn(flux);

            MvcResult mvcResult = mockMvc.perform(get("/users/{userId}/recommended", user.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

            verify(service, times(1))
                .findRecommendedMoviesGiven(anyString());
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
