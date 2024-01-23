package com.fiap.alegorflix.utils;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.fiap.alegorflix.movie.controller.dto.MovieDto;
import com.fiap.alegorflix.movie.entity.CategoryEnum;
import com.fiap.alegorflix.movie.entity.Movie;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class MovieHelper {
    public static Movie createMovie(){
        return Movie.builder()
            .id("1")
            .title("Scream")
            .description("Slasher")
            .category("Horror")
            .url("scream.com.br")
            .publishedDate(LocalDate.now())
            .views(1000)
            .version(1L)
            .build();
    }

    public static List<Movie> createListMovies(){
        return List.of(createMovie());
    }
    public static Mono<PageImpl<Movie>> createPageOfMovies(List<Movie> listMovies, Pageable pageable){
        return Flux.fromIterable(listMovies)
                .collectList()
                .map(movies -> new PageImpl<>(movies, pageable, movies.size()));
    }
    public static MovieDto createMovieDTO(){
        MovieDto movieDto = new MovieDto("Scream", "Slasher Film", "scream.com.br", CategoryEnum.HORROR);
        return movieDto;
    }

}
