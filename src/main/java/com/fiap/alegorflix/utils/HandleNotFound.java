package com.fiap.alegorflix.utils;

import com.fiap.alegorflix.exception.NotFoundException;

import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

@UtilityClass
public class HandleNotFound {

    public static <T> Mono<T> handleNotFound(Mono<T> mono, String id) {

        String errorMsg = String.format("Could not find any object given Id: %s", id);
        return mono.switchIfEmpty(Mono.error(new NotFoundException(errorMsg)));
    }
}
