package com.fiap.alegorflix.user.controller;

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

import com.fiap.alegorflix.user.controller.dto.UserDto;
import com.fiap.alegorflix.user.entity.User;
import com.fiap.alegorflix.user.service.UserService;

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
@RequestMapping("/users")
@Tag(name = "Users", description = "Methods for manipulating User's data")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "BAD REQUEST - Client error", content = @Content(examples = {
                @ExampleObject(summary = "Bad Request", value = "{\"statusCode\":400,\"message\":\"Bad Request\"}")
        }, mediaType = MediaType.APPLICATION_JSON_VALUE)),
        @ApiResponse(responseCode = "404", description = "NOT FOUND - User Id not Found", content = @Content(examples = {
                @ExampleObject(summary = "User ID not found", value = "{\"statusCode\":404,\"message\":\"User ID not found\"}")
        }, mediaType = MediaType.APPLICATION_JSON_VALUE)),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR - Something went wrong", content = @Content(examples = {
                @ExampleObject(summary = "Internal Server Error", value = "{\"statusCode\":500,\"message\":\"Internal Server Error\"}")
        }, mediaType = MediaType.APPLICATION_JSON_VALUE))
})
public class UserController {

    private final UserService service;

    @Operation(summary = "Get all the Users", description = "Method for getting all the Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SUCCESS - List of all Users", content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping
    public ResponseEntity<Mono<PageImpl<User>>> findAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));

        return ResponseEntity.status(OK).body(service.findAll(pageRequest));
    }

    @Operation(summary = "Get a User by ID", description = "Method to get a User based on the ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = User.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
    })
    @GetMapping("{id}")
    public ResponseEntity<Mono<User>> getById(@PathVariable("id") String userId) {
        Mono<User> user = service.findById(userId);
        return new ResponseEntity<>(user, OK);
    }

    @Operation(summary = "Create a User", description = "Method to crete a new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SUCCESS - User successfully created", content = @Content(schema = @Schema(implementation = User.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
    })
    @PostMapping
    public ResponseEntity<Mono<User>> create(@Valid @RequestBody UserDto user) {
        Mono<User> savedItem = service.create(user);
        return new ResponseEntity<>(savedItem, CREATED);
    }

    @Operation(summary = "Update a User", description = "Method to update an existing User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SUCCESS - User successfully updated", content = @Content(schema = @Schema(implementation = User.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
    })
    @PutMapping("{id}")
    public ResponseEntity<Mono<User>> update(@PathVariable("id") String id, @Valid @RequestBody UserDto user) {
        Mono<User> updatedUser = service.update(id, user);
        return new ResponseEntity<>(updatedUser, OK);
    }

    @Operation(summary = "Delete a User", description = "Method to Delete an existing User")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id) {
        service.deleteById(id);
        String message = "User " + id + " deleted with success";
        return new ResponseEntity<>(message, OK);
    }

}
