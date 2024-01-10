package com.fiap.alegorflix.utils;

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
            .publishedDate(LocalDate.of(1993,01,01))
            .version(1L)
            .build();
    }
}
