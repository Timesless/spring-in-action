package com.yangzl.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

/**
 * @author yangzl
 * @date 2020/8/27 10:46
 * @desc
 *      用这两个bean来演示spring 启动流程
 */

@Configuration
public class IntegrationBean {

    @Bean
    public MessageChannel inChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel outChannel() {
        return new DirectChannel();
    }
}
