package com.fiap.alegorflix.movie.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fiap.alegorflix.movie.controller.dto.MovieDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    private String id;
    private String title;
    private String description;
    private String category;
    private String url;
    private LocalDate publishedDate;

    @Version
    private Long version;

    public Movie(MovieDto dto) {
        this.title = dto.title();
        this.description = dto.description();
        this.category = dto.category().getCategory();
        this.url = dto.url();
        this.publishedDate = LocalDate.now();
    }

}
