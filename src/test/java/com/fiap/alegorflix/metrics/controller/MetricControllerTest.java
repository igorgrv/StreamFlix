package com.fiap.alegorflix.metrics.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fiap.alegorflix.metrics.service.MetricService;
import com.fiap.alegorflix.utils.MetricHelper;

import reactor.core.publisher.Mono;

public class MetricControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MetricService service;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class FindMetric {

        @Test
        void shouldAllowFindMetric() throws Exception {
            var metric = MetricHelper.createMetric();
            Mono mono = Mono.just(metric);
            when(service.getMetrics())
                .thenReturn(mono);

            mockMvc.perform(get("/metric")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncResult(metric))
                .andExpect(status().isOk());

            verify(service, times(1))
                .getMetrics();
        }
    }
}
