package com.fiap.alegorflix.metrics.controller;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.alegorflix.metrics.entity.Metric;
import com.fiap.alegorflix.metrics.service.MetricService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/metric")
@Tag(name = "Metrics", description = "Methods to collect metrics")
@RequiredArgsConstructor
public class MetricController {

    private final MetricService metricService;

    @Operation(summary = "Get the number of movies, favorite movies and average of movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Metric.class)), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping
    public ResponseEntity<Mono<Metric>> getMetrics() {
        Mono<Metric> metric = metricService.getMetrics();
        return new ResponseEntity<>(metric, OK);
    }

}
