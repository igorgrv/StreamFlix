package com.fiap.alegorflix.user.controller;

import com.fiap.alegorflix.user.entity.User;
import com.fiap.alegorflix.utils.UserHelper;
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
public class UserControllerIT {
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
    class CreateUser {

        @Test
        void shouldCreateAUser() {
            mongoTemplate.dropCollection("user");
            var userRequest = UserHelper.createUserDto();

            given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(userRequest)
                .when()
                .post("/users")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("$", hasKey("name"))
                .body("name", equalTo(userRequest.name()));

        }
    }

    @Nested
    @Order(2)
    class FindUser {

        @Test
        void shouldFindAllUsers() {

            given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("page", 0, "size", 10, "sortBy", "name")
                .when()
                .get("/users")
                .then()
                .statusCode(HttpStatus.OK.value());
        }

        @Test
        void shouldFindAUserById() {
            List<User> userList = mongoTemplate.findAll(User.class);

            given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/users/{id}", userList.get(0).getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        }

        @Test
        void shouldNotFindAUserIdNotFound() {

            given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/users/{id}", "2")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

        }

        @Test
        void shouldFindARecommendedMoviesGivenUser() {
            List<User> userList = mongoTemplate.findAll(User.class);

            given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/users/{userId}/recommended", userList.get(0).getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        }

    }

    @Nested
    @Order(3)
    class UpdateUser {

        @Test
        void shouldUpdateAUser() {
            List<User> userList = mongoTemplate.findAll(User.class);
            userList.get(0).setEmail("emailupdated@gmail.com");

            given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(UserHelper.UserToUserDto(userList.get(0)))
                .when()
                .put("/users/{id}", userList.get(0).getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasKey("email"))
                .body("email", equalTo("emailupdated@gmail.com"));

        }

    }
    @Nested
    @Order(4)
    class DeleteUser {

        @Test
        void shouldDeleteAUser() {
            List<User> userList = mongoTemplate.findAll(User.class);

            given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/users/{id}", userList.get(0).getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        }
    }

}
