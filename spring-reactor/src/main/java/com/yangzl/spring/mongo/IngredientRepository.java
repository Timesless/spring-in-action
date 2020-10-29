package com.yangzl.spring.mongo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * @Author yangzl
 * @Date 2020/9/3 14:22
 * @Desc
 */
public interface IngredientRepository extends ReactiveCrudRepository<Ingredient, String> {

    Mono<Ingredient> findIngredientById(long id);
}
