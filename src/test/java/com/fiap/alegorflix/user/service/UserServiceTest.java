
package com.fiap.alegorflix.user.service;

import com.fiap.alegorflix.movie.entity.Movie;
import com.fiap.alegorflix.movie.service.MovieService;
import com.fiap.alegorflix.user.entity.User;
import com.fiap.alegorflix.user.repository.UserRepository;
import com.fiap.alegorflix.utils.MovieHelper;
import com.fiap.alegorflix.utils.UserHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository repository;

    @Mock
    private User user;

    @Mock
    private MovieService movieService;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        userService = new UserService(repository, movieService);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class findUsers {

        @Test
        void shouldFindAllUsers() {

            Flux<User> flux = Flux.just(UserHelper.createUser());

            when(repository.findAllBy(any()))
                .thenReturn(flux);

            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

            var users = userService.findAll(pageRequest);

            assertThat(users.block()).hasSize(1);
            assertThat(users.block().getContent())
                .asList()
                .allSatisfy(user -> {
                    assertThat(user).isNotNull();
                    assertThat(user).isInstanceOf(User.class);
                });

            verify(repository, times(1)).findAllBy(pageRequest);
        }

        @Test
        void shouldFindById() {

            Mono<User> mono = Mono.just(UserHelper.createUser());

            when(repository.findById(anyString()))
                .thenReturn(mono);

            var user = userService.findById(mono.block().getId());

            verify(repository, times(1))
                .findById(mono.block().getId());
            assertThat(user.block().getId())
                .isEqualTo(mono.block().getId());
        }

        @Test
        void shouldRecommendedMoviesGiven() {
            Mono<Movie> monoMovie = Mono.just(MovieHelper.createMovie());
            Mono<User> monoUser = Mono.just(UserHelper.createUser());

            when(repository.findById(anyString()))
                .thenReturn(monoUser);
            when(movieService.findByCategories(anySet())).thenReturn(Flux.just(monoMovie.block()));

            var user = userService.findRecommendedMoviesGiven(monoUser.block().getId());

            verify(repository, times(1))
                .findById(monoUser.block().getId());
        }


        @Test
        void shouldGetNumberFavoriteMovies() {

            Mono<User> monoUser = Mono.just(UserHelper.createUser());

            when(repository.findAll())
                .thenReturn(Flux.just(monoUser.block()));

            when(user.getFavoriteMovies())
                .thenReturn(Set.of(MovieHelper.createMovie()));

            userService.getNumberFavoriteMovies();

            verify(repository, times(1))
                .findAll();
        }
    }

    @Nested
    class createUser {

        @Test
        void shouldCreateUser() {

            var user = UserHelper.createUserDto();
            when(repository.save(any(User.class)))
                .thenReturn(Mono.just(UserHelper.createUser()));

            var userCreated = userService.create(user);

            assertThat(userCreated)
                .isNotNull();
            assertThat(userCreated.block().getName())
                .isEqualTo(user.name());
            verify(repository, times(1)).save(any(User.class));
        }


        @Test
        void shouldAddFavoriteMovie() {

            var user = Mono.just(UserHelper.createUser());
            user.block().setFavoriteMovies(null);
            var movie = Mono.just(MovieHelper.createMovie());

            when(repository.findById(anyString()))
                .thenReturn(user);
            when(repository.save(any(User.class)))
                .thenReturn(user);
            when(movieService.findById(anyString()))
                .thenReturn(movie);

            var userUpdated = userService.addFavoriteMovie(user.block().getId(), movie.block().getId());

            assertThat(userUpdated)
                .isNotNull();
        }

    }

    @Nested
    class updateUser {

        @Test
        void shouldUpdateUser() {

            var userDTO = UserHelper.createUserDto();
            var user = UserHelper.createUser();

            when(repository.findById(anyString()))
                .thenReturn(Mono.just(user));
            when(repository.save(any(User.class)))
                .thenReturn(Mono.just(user));

            var userUpdated = userService.update(user.getId(), userDTO);

            assertThat(userUpdated)
                .isNotNull();
            assertThat(userUpdated.block().getName())
                .isEqualTo(userDTO.name());
            verify(repository, times(1)).save(any(User.class));
        }

    }

    @Nested
    class deleteUser {
        @Test
        void shouldDeleteUser() {

            when(repository.deleteById(anyString()))
                .thenReturn(Mono.empty());

            userService.deleteById("1");

            verify(repository, times(1)).deleteById(anyString());
        }
    }
}
