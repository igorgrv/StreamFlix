package com.fiap.alegorflix.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.RedirectView;

import reactor.core.publisher.Mono;

@Controller
public class SwaggerController {

    @GetMapping("/")
    public Mono<RedirectView> redirectToSwagger() {
        return Mono.just(new RedirectView("/webjars/swagger-ui/index.html"));
    }
}
