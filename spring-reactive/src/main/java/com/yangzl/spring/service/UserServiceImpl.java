package com.yangzl.spring.service;

import com.yangzl.spring.entity.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangzl
 * @date 2020/12/19 11:34
 * @desc
 */

@Service
public class UserServiceImpl implements UserService {
    
    private final Map<Integer, User> map = new ConcurrentHashMap<>(64);
    {
        map.put(1, new User(0, "hhh", 24, 0));
    }

    @Override
    public Mono<User> queryById(Integer id) {
        return Mono.justOrEmpty(map.get(id));
    }

    @Override
    public Flux<User> queryByList() {
        return Flux.fromIterable(map.values());
    }

    @Override
    public Mono<Void> save(Mono<User> userMono) {
        return userMono.doOnNext(user -> {
            map.put(map.size(), user);
        }).thenEmpty(Mono.empty());
    }
}
