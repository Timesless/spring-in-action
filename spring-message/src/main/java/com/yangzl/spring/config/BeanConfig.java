package com.yangzl.spring.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Destination;

/**
 * @Author yangzl
 * @Date 2020/8/27 10:46
 * @Desc
 */

@Configuration
public class BeanConfig {

    /**
     * JmsQueue 多个queue配置
     * order queue
     * mapQueue 默认值
     *
     * ActiveMQ有两种方式的消息
     *  点对点：1 - 1，Queue模式
     *  发布订阅： 1 - N，Topic模式
     */
    @Bean
    public Destination logQueue() {
        return new ActiveMQQueue("jms.queue.log");
    }
}
