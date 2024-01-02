package com.fiap.alegorflix.utils;

import com.fiap.alegorflix.exception.NotFoundException;

import lombok.experimental.UtilityClass;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@UtilityClass
public class HandleNotFound {

    public static <T> Mono<T> handleNotFound(Mono<T> mono, Class<T> elementType, String id) {

        String errorMsg = String.format("Could not find any %s given: %s", elementType.getSimpleName(), id );
        return mono.switchIfEmpty(Mono.error(new NotFoundException(errorMsg)));
    }

    public static <T> Flux<T> handleNotFoundFlux(Flux<T> flux, Class<T> elementType, Object id) {
        String errorMsg = String.format("Could not find any %s given: %s", elementType.getSimpleName(), id);
        return flux.switchIfEmpty(Flux.error(new NotFoundException(errorMsg)));
    }
}
