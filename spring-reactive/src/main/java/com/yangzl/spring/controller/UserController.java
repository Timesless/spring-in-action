package com.yangzl.spring.controller;

import com.yangzl.spring.entity.User;
import com.yangzl.spring.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author yangzl
 * @date 2020/12/19 11:31
 * @desc 基于注解方式使用WebFlux
 */

@RestController
public class UserController {
    
    @Resource
    private UserService userService;
    
    @GetMapping("/user/{id}")
    public Mono<User> queryById(@PathVariable Integer id) {
        return userService.queryById(id);
    }
    
    @GetMapping("/users")
    public Flux<User> queryByList() {
        return userService.queryByList();
    }
    
    @PostMapping("/user/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> save(@RequestBody User user) {
        return userService.save(Mono.just(user));
    }
    
}
