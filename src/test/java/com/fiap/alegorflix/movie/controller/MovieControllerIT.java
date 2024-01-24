package com.fiap.alegorflix.movie.controller;

import com.fiap.alegorflix.movie.entity.Movie;
import com.fiap.alegorflix.utils.MovieHelper;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

@AutoConfigureDataMongo
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class MovieControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    MongoTemplate mongoTemplate;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Nested
    @Order(1)
    class CreateMovie {

        @Test
        void shouldCreateAMovie() {
            mongoTemplate.dropCollection("movie");
            var movieRequest = MovieHelper.createMovieDTO();

            given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(movieRequest)
                .when()
                .post("/movies")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("$", hasKey("title"))
                .body("$", hasKey("description"))
                .body("title", equalTo(movieRequest.title()))
                .body("description", equalTo(movieRequest.description()));

        }

        @Test
        void shouldNotCreateAMovieWhenMovieAlreadyExist() {
            var movieRequest = MovieHelper.createMovieDTO();

            given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(movieRequest)
                .when()
                .post("/movies")
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("$", hasKey("message"))
                .body("message", equalTo("Movie with title 'Scream' already exists"));
        }
    }

    @Nested
    @Order(2)
    class FindMovie {

        @Test
        void shouldFindAllMovies() {

            given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("page", 0, "size", 10, "sortBy", "publishedDate")
                .when()
                .get("/movies")
                .then()
                .statusCode(HttpStatus.OK.value());
        }

        @Test
        void shouldFindAMovieById() {
            List<Movie> movieList = mongoTemplate.findAll(Movie.class);

            given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/movies/{id}", movieList.get(0).getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        }

        @Test
        void shouldNotFindAMovieIdNotFound() {

            given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/movies/{id}","2")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

        }
    }

}
