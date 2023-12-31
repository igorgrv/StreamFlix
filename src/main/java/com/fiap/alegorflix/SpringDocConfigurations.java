package com.fiap.alegorflix;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(info = @Info(title = "AlegorFlix Monitor", version = "0.0.1", description = "Solution for monitoring AlegorFlix", license = @License(name = "MIT License", url = "https://github.com/igorgrv/alegorflix-graduate")), servers = {
    @Server(url = "http://localhost:8080"),
    @Server(url = "https://alegorflix-e2kw5mlx.b4a.run"),
        @Server(url = "https://localhost:8080")
})
public class SpringDocConfigurations {
}
