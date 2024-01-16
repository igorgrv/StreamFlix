package com.fiap.alegorflix.utils;

import com.fiap.alegorflix.movie.controller.dto.MovieDto;
import com.fiap.alegorflix.movie.entity.CategoryEnum;
import com.fiap.alegorflix.movie.entity.Movie;

import java.time.LocalDate;

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
    public static MovieDto createMovieDTO(){
        MovieDto movieDto = new MovieDto("Scream", "Slasher Film", "scream.com.br", CategoryEnum.HORROR);
        return movieDto;
    }

}
