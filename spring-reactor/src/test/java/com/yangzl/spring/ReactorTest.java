package com.yangzl.spring;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author yangzl
 * @Date: 2020/9/1 10:26
 * @Desc:
 *
 * Flux和Mono共有500个操作，大致分类为（感觉和stream操作比较类似）
 *  1. 创建
 *  2. 组合
 *  3. 转换
 *  4. 逻辑
 */
public class ReactorTest {

    /**
     * 2020/9/1 创建与测试反应式流
     * @param
     * @return
     */
    @Test
    public void reactor1() {
        // 无法依据上下文推断时，声明参数化类型
        Flux<String> flux = Flux.<String>just("reactor", "reactive x");
        flux.subscribe(System.out::println);
        StepVerifier.create(flux)
                .expectNext("reactor")
                .expectNext("reactive x")
                .verifyComplete();
    }

    /**
     * 2020/9/1 创建反应式流，通过集合、数组、stream创建Flux
     * @param
     * @return
     */
    @Test
    public void testReactor2() {
        List<String> list = Arrays.asList("fulx", "mono", "observable", "single");
        String[] array = {"flux", "mono", "obervable", "single"};
        Flux.fromIterable(list).subscribe(System.out::println);
        Flux.fromArray(array).subscribe(System.out::println);
        Flux.fromStream(Arrays.stream(array)).subscribe(System.out::println);

        // 注意，这里不是左闭右开，而采用左闭右闭
        Flux.range(2, 4).subscribe(System.out::print);
        System.out.println();

        // 时间周期创建Flux
        Flux<Long> intervalFlux = Flux.interval(Duration.ofMillis(200)).take(4);
        intervalFlux.subscribe(System.out::print);

        StepVerifier.create(intervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .verifyComplete();
    }


    /**
     * 2020/9/1 组合反应式流
     * delayElements()每500毫秒发布一个条目。
     * 此外，为了使fruitFlux后发布，我们调用delaySubscription()，以便它在订阅后再经过100毫秒后才开始发布数据。
     * @param
     * @return
     */
    @Test
    public void testReactor3() {
        Flux<String> cFlux = Flux.just("G", "K", "B")
                .delayElements(Duration.ofMillis(200));
        Flux<String> fruitFlux = Flux.just("orange", "strawbery", "banana")
                .delayElements(Duration.ofMillis(200))
                .delaySubscription(Duration.ofMillis(100));
        cFlux.mergeWith(fruitFlux).subscribe(System.out::println);

        // 产生一个新的发布元组（Tuple2 ~ Tuple8）的Flux
        Flux<Tuple2<String, String>> zipFlux = Flux.zip(cFlux, fruitFlux);
        StepVerifier.create(zipFlux)
                .expectNextMatches(t -> t.getT1().equals("G") && t.getT2().equals("orange"));
        // 如果不想生成元组，也可以使用一个BiFunction来指定

        // first，选择快的Flux进行发布，这里会发布cFlux
        Flux.first(cFlux, fruitFlux).subscribe(System.out::println);

        try { TimeUnit.SECONDS.sleep(1); } catch(InterruptedException e) { e.printStackTrace(); }
    }

    /**
     * 2020/9/1 转换、过滤 反应式流
     * @param
     * @return 
     */
    @Test
    public void testReactor4() {
        Flux<String> flux = Flux.just("one", "two", "skip a few", "ninety-nine", "one hundred");

        System.out.println("==== take ====");
        flux.take(3).subscribe(System.out::print);
        System.out.println();

        System.out.println("==== skip ====");
        flux.skip(3).subscribe(System.out::print);
        System.out.println();

        // 也可以skip Duration
        // take 同理

        flux.delayElements(Duration.ofMillis(100))
                .skip(Duration.ofMillis(200))
                .subscribe(System.out::print);
        // filter
        System.out.println("==== filter ====");
        flux.filter(e -> e.contains("n")).subscribe(System.out::println);

        // distinct 去重
        System.out.println("==== distinct ====");
        Flux.just("1", "1").distinct().subscribe(System.out::println);

        // map
        System.out.println("==== map ====");
        flux.map(String::length).subscribe(System.out::println);
        // map为Tuple
        System.out.println("==== map 2 tuple ====");
        Flux.just("Kobe Bryant", "Michael Jordan").map(e -> {
            String[] names = e.split("\\s");
            return Tuples.of(names[0], names[1]);
        }).subscribe(System.out::println);

    }
    
    
}
