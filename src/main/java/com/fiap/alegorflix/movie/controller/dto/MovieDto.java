package com.fiap.alegorflix.movie.controller.dto;

import com.fiap.alegorflix.movie.entity.CategoryEnum;
import com.fiap.alegorflix.movie.entity.Movie;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(title = "MovieDto", description = "Object that represents a Movie's data transfer object")
public record MovieDto(  
    @NotBlank(message = "title is mandatory")
    @Size(min = 2, max = 50, message = "size must be between {min} and {max}")
    @Schema(description = "title of the movie", example = "Harry Potter")
    String title,

    @NotBlank(message = "description is mandatory")
    @Size(min = 10, max = 200, message = "size must be between {min} and {max}")
    @Schema(description = "description of the movie", example = "An orphaned boy enrolls in a school of wizardry, where he learns the truth about himself, his family and the terrible evil that haunts the magical world")
    String description,

    @NotBlank(message = "url is mandatory")
    @Size(min = 6, max = 200, message = "size must be between {min} and {max}")
    @Schema(description = "url of the movie", example = "https://www.alegorflix.com/harrypotter")
    String url,

    @NotNull(message = "category is mandatory")
    @Schema(description = "category of the movie", example = "SCIENCE_FICTION")
    CategoryEnum category) {

    public Movie getMovieUpdated(Movie oldMovie) {
        oldMovie.setTitle(title);
        oldMovie.setDescription(description);
        oldMovie.setCategory(category.getCategory());
        return oldMovie;
    }

}
