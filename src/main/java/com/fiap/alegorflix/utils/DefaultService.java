package com.fiap.alegorflix.utils;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import reactor.core.publisher.Mono;

public interface DefaultService<R, D> {

    Mono<PageImpl<R>> findAll(Pageable pageable);
    Mono<R> findById(final String id);
    Mono<R> create(final D request);
    Mono<R> update(final String id, final D dto);
    Mono<Void> deleteById(final String id);

}
