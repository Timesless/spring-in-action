package com.yangzl.spring.autoconfigure.threadpool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @author yangzl
 * @date 2021/1/21
 * @desc 自定义线程池自动配置类
 */
@Configuration
// ?
@ConditionalOnClass(ThreadPoolExecutor.class)
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class ThreadPoolAutoConfiguration {

    /** 阻塞队列 */
    private final BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(64);

    /** maxPoolSize + workQueue full 拒绝策略 */
    private final RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

    private int corePoolSize;

    private int maxPoolSize;

    /** CPU密集，1是 0否 */
    @Value("${thread.pool.cpu-intensive}")
    private int cpuIntensive;

    private long keepAliveTime;

    private TimeUnit timeUnit;

    @PostConstruct
    public void init() {
        int cpu = Runtime.getRuntime().availableProcessors();
        this.corePoolSize = cpu;
        this.maxPoolSize = cpu << 3;
        this.keepAliveTime = 60L * 2;
        this.timeUnit = TimeUnit.SECONDS;
    }

    /**
     * 构建的线程池
     *
     * @return ThreadPoolExecutor
     */
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        // CPU密集型 core + 1， I/O密集 core * 2
        int core = cpuIntensive == 1 ? corePoolSize + 1 : corePoolSize << 1;
        return new ThreadPoolExecutor(core, maxPoolSize, keepAliveTime, timeUnit, workQueue, handler);
    }

}
