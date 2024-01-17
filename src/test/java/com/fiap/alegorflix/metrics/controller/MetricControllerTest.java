package com.fiap.alegorflix.metrics.controller;

import com.fiap.alegorflix.exception.CustomExceptionHandler;
import com.fiap.alegorflix.metrics.service.MetricService;
import com.fiap.alegorflix.utils.MetricHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MetricControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MetricService service;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        MetricController metricController = new MetricController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(metricController)
            .setControllerAdvice(new CustomExceptionHandler())
            .addFilter((request, response, chain) -> {
                response.setCharacterEncoding("UTF-8");
                chain.doFilter(request, response);
            }, "/*")
            .build();
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
