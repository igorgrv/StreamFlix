package com.fiap.alegorflix.movie.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.alegorflix.movie.controller.dto.MovieDto;
import com.fiap.alegorflix.movie.entity.Movie;
import com.fiap.alegorflix.movie.service.MovieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/movies")
@Tag(name = "Movies", description = "Methods for manipulating Movie's data")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "BAD REQUEST - Client error", content = @Content(examples = {
                @ExampleObject(summary = "Bad Request", value = "{\"statusCode\":400,\"message\":\"Bad Request\"}")
        }, mediaType = MediaType.APPLICATION_JSON_VALUE)),
        @ApiResponse(responseCode = "404", description = "NOT FOUND - Movie Id not Found", content = @Content(examples = {
                @ExampleObject(summary = "Movie ID not found", value = "{\"statusCode\":404,\"message\":\"Movie ID not found\"}")
        }, mediaType = MediaType.APPLICATION_JSON_VALUE)),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR - Something went wrong", content = @Content(examples = {
                @ExampleObject(summary = "Internal Server Error", value = "{\"statusCode\":500,\"message\":\"Internal Server Error\"}")
        }, mediaType = MediaType.APPLICATION_JSON_VALUE))
})
public class MovieController {

    private final MovieService service;

    @Operation(summary = "Get all the Movies", description = "Method for getting all the Movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SUCCESS - List of all Movies", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Movie.class)), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping
    public ResponseEntity<Mono<PageImpl<Movie>>> findAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));

        return ResponseEntity.status(OK).body(service.findAll(pageRequest));
    }

    @Operation(summary = "Get a Movie by ID", description = "Method to get a Movie based on the ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = Movie.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
    })
    @GetMapping("{id}")
    public ResponseEntity<Mono<Movie>> getById(@PathVariable("id") String movieId) {
        Mono<Movie> movie = service.findById(movieId);
        return new ResponseEntity<>(movie, OK);
    }

    @Operation(summary = "Create a Movie", description = "Method to crete a new Movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SUCCESS - Movie successfully created", content = @Content(schema = @Schema(implementation = Movie.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
    })
    @PostMapping
    public ResponseEntity<Mono<Movie>> create(@Valid @RequestBody MovieDto movie) {
        Mono<Movie> savedItem = service.create(movie);
        return new ResponseEntity<>(savedItem, CREATED);
    }

    @Operation(summary = "Update a Movie", description = "Method to update an existing Movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SUCCESS - Movie successfully updated", content = @Content(schema = @Schema(implementation = Movie.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
    })
    @PutMapping("{id}")
    public ResponseEntity<Mono<Movie>> update(@PathVariable("id") String id, @Valid @RequestBody MovieDto movie) {
        Mono<Movie> updatedMovie = service.update(id, movie);
        return new ResponseEntity<>(updatedMovie, OK);
    }

    @Operation(summary = "Delete a Movie", description = "Method to Delete an existing Movie")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id) {
        service.deleteById(id);
        String message = "Movie " + id + " deleted with success";
        return new ResponseEntity<>(message, OK);
    }

}
