package com.fiap.alegorflix.user.service;

import static com.fiap.alegorflix.utils.HandleNotFound.handleNotFound;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fiap.alegorflix.user.controller.dto.UserDto;
import com.fiap.alegorflix.user.entity.User;
import com.fiap.alegorflix.user.repository.UserRepository;
import com.fiap.alegorflix.utils.DefaultService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService implements DefaultService<User, UserDto> {

    private final UserRepository repository;

    public Mono<PageImpl<User>> findAll(Pageable pageable) {
        return repository.findAllBy(pageable)
                .collectList()
                .map(users -> new PageImpl<>(users, pageable, users.size()));
    }

    public Mono<User> findById(String userId) {
        return handleNotFound(repository.findById(userId), User.class, userId);
    }

    public Mono<User> create(UserDto user) {
        return repository.save(new User(user));
    }

    public Mono<User> update(String id, UserDto user) {
        return findById(id)
                .map(user::getUserUpdated)
                .flatMap(repository::save);
    }

    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id).then();
    }

}
