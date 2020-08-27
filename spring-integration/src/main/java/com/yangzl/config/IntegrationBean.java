package com.yangzl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

/**
 * @Author yangzl
 * @Date: 2020/8/27 10:46
 * @Desc:
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
