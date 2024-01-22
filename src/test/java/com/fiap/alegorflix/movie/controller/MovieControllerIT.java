package com.fiap.alegorflix.movie.controller;

import com.fiap.alegorflix.utils.MovieHelper;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

@AutoConfigureDataMongo
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
    class RegistrarMensagem {

        @Test
        void devePermitirRegistrarMensagem() {
            mongoTemplate.dropCollection("movie");
            var movieRequest = MovieHelper.createMovieDTO();

            given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(movieRequest)
                .when()
                .post("/movies")
                .then()
                .statusCode(HttpStatus.CREATED.value());
//                .body("$", hasKey("id"))
//                .body("$", hasKey("usuario"))
//                .body("$", hasKey("conteudo"))
//                .body("$", hasKey("dataCriacao"))
//                .body("$", hasKey("gostei"))
//                .body("usuario", equalTo(mensagemRequest.getUsuario()))
//                .body("conteudo", equalTo(mensagemRequest.getConteudo()));

            System.out.print("Teste");
        }
    }
}
