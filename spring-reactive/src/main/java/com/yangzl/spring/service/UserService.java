package com.yangzl.spring.service;

import com.yangzl.spring.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author yangzl
 * @date 2020/12/19 11:33
 * @desc
 */
public interface UserService {
    
    /**
     * 2020/12/19 通过id查询用户
     * 
	 * @param id userid
     * @return Mono
     */
    Mono<User> queryById(Integer id);
    
    /**
     * 2020/12/19 查询用户列表
     * 
	 * @param () v
     * @return Flux
     */
    Flux<User> queryByList();
    
    /**
     * 2020/12/19
     * 
	 * @param userMono 用户信息 Publisher
     * @return Mono
     */
    Mono<Void> save(Mono<User> userMono);
}
