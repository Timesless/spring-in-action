package com.yangzl.spring.functional;

import com.yangzl.spring.entity.User;
import com.yangzl.spring.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author yangzl
 * @date 2020/12/19 14:50
 * @desc HandlerFunction 处理实际请求并构建响应
 */

public class UserHandler {
    
    private final UserService userService;
    public UserHandler(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * 2020/12/19 根据ID查询用户
     * 
	 * @param request reactor请求
     * @return Mono
     */
    public Mono<ServerResponse> queryById(ServerRequest request) {
        final int id = Integer.parseInt(request.pathVariable("id"));
        Mono<User> userMono = userService.queryById(id);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userMono, User.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    
    /**
     * 2020/12/19 查询用户列表
     * 
	 * @param () v
     * @return Mono
     */
    public Mono<ServerResponse> queryList(ServerRequest request) {
        Flux<User> users = userService.queryByList();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(users, User.class);
    }
    
    public Mono<ServerResponse> save(ServerRequest request) {
        final Mono<User> userMono = request.bodyToMono(User.class);
        return ServerResponse.ok()
                .build(userService.save(userMono));
    }
}
