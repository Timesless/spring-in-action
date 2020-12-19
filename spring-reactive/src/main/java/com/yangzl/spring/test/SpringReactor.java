package com.yangzl.spring.test;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author yangzl
 * @date 2020/12/18 22:34
 * @desc io.projectreactor.reactive
 * 
 *      核心API
 *          {@link reactor.core.publisher.Mono} 0..1
 *          {@link reactor.core.publisher.Flux} 0..N
 *          
 *      Flux 和 Mono 都是数据流的发布者 【Publisher】，可以发出三种信号
 *          1. 元素值
 *          2. 错误信号
 *          3. 完成信号
 *      错误信号和完成信号都是终止信号，错误信号会将异常传递给订阅者【Subscriber】
 *      
 *      map 对每个数据应用函数
 *      flatMap 将每个数据应用函数且扁平化为流
 *      
 *
 *      WebFlux 重要概念
 *          WebServerExchange
 *          DisPatcherHandler
 *          RouterFunction：实现路由
 *          HandlerFunction：具体执行业务
 */

public class SpringReactor {
    
    @Test
    public void testMonoFlux() {
        Mono<Integer> m = Mono.just(1);
        Flux.<Integer>just(1, 2, 3);
        
        Integer[] arr = { 1, 2, 3, 4 };
        // 从数组 / 流 / 集合构建Flux
        Flux.fromArray(arr);
        Flux.fromStream(Stream.iterate(1, x -> x + 1).limit(5));
        Flux.fromIterable(Arrays.asList(arr));
        
        // 只有添加订阅者才会调用
        m.subscribe(System.out::println);
    }
    
}
