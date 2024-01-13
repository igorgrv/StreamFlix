package com.fiap.alegorflix.user.entity;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fiap.alegorflix.movie.entity.Movie;
import com.fiap.alegorflix.user.controller.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private LocalDate createdDate;
    private Set<Movie> favoriteMovies;

    @Version
    private Long version;

    public User(UserDto dto) {
        this.name = dto.name();
        this.email = dto.email();
        this.password = dto.password();
        this.createdDate = LocalDate.now();
    }

}
