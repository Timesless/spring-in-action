package com.yangzl.spring.functional;

import com.yangzl.spring.entity.User;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author yangzl
 * @date 2020/12/19 16:04
 * @desc 使用WebClient进行Reactor请求
 */

public class Client {

    public static void main(String[] args) {
        final WebClient client = WebClient.create("127.0.0.1:8082");
        final User user = client.get().uri("/user/{id}").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(User.class)
                // 阻塞获取
                .block();
        System.out.println(user);
        
        client.get().uri("/users").accept(MediaType.APPLICATION_JSON).retrieve()
                .bodyToFlux(User.class)
                .map(User::getName)
                // Collect all incoming values into a single List buffer that will be emitted by the returned Flux once this Flux completes.
                .buffer()
                .doOnNext(System.out::println)
                .blockFirst();
    }
    
}
