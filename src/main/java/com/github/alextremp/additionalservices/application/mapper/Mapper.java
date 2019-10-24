package com.github.alextremp.additionalservices.application.mapper;

import reactor.core.publisher.Mono;

public interface Mapper<I, O> {

  Mono<O> map(I input);
}
