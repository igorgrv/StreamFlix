package com.fiap.alegorflix.movie.entity;

import java.time.LocalDate;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fiap.alegorflix.movie.controller.dto.MovieDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Movie {

    @Id
    private String id;

    @Indexed(unique = true)
    private String title;
    private String description;
    private String category;
    private String url;
    private LocalDate publishedDate;
    private Integer views = 0;

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
