package com.fiap.alegorflix.utils;

import com.fiap.alegorflix.movie.controller.dto.MovieDto;
import com.fiap.alegorflix.movie.entity.CategoryEnum;
import com.fiap.alegorflix.user.controller.dto.UserDto;
import com.fiap.alegorflix.user.entity.User;

import java.time.LocalDate;
import java.util.Set;

public abstract class UserHelper {
    public static User createUser(){
        return User.builder()
            .id("1")
            .name("Igor")
            .email("igor@gmail.com")
            .password("igor123")
            .createdDate(LocalDate.now())
            .favoriteMovies(Set.of(MovieHelper.createMovie()))
            .build();
    }

    public static UserDto createUserDto(){
        return new UserDto("Igor", "igor@gmail.com", "igor123");
    }

    public static UserDto UserToUserDto(User user){
        return new UserDto(user.getName(), user.getEmail(), user.getPassword());
    }
}
