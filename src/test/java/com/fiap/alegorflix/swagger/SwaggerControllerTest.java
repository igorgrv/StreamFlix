package com.fiap.alegorflix.swagger;

import com.fiap.alegorflix.exception.CustomExceptionHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.reactive.result.view.RedirectView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SwaggerControllerTest {

    private MockMvc mockMvc;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        SwaggerController swaggerController = new SwaggerController();
        mockMvc = MockMvcBuilders.standaloneSetup(swaggerController)
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
    class RedirectToSwagger {

        @Test
        void shouldAllowFindMetric() throws Exception {
            RedirectView redirectView = new RedirectView("/webjars/swagger-ui/index.html");

            mockMvc.perform(get("/")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }
}
