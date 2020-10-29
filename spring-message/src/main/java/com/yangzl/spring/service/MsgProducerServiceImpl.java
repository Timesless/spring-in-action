package com.yangzl.spring.springinaction.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author yangzl
 * @Date: 2020/8/27 15:53
 * @Desc:
 *  RabbitMQ 由 Erlang开发， 安装前需安装Erlang
 */

@Slf4j
@Component
public class MsgProducerServiceImpl implements MsgProducerService {
    private Map<String, Object> map;
    {
        map = new HashMap<>(4);
        map.put("id", 123);
        map.put("name", 123);
    }

    /**
     * KafkaTempalte，在 profile 为 kafka时启用
     * 3种注入方式
     *  构造器注入
     *  set注入
     *  属性注入（不推荐），属性注入的类在DI容器之外调用可能NPE
     */
    private KafkaTemplate<String, String> kafaka;
    @Autowired
    @Profile("kafka")
    public void setKafaka(KafkaTemplate<String, String> kafaka) {
        log.info("=============初始化 KafkaTemplate====================");
        this.kafaka = kafaka;
    }
    private RabbitTemplate rabbitTemplate;
    @Autowired
    @Profile("amqp")
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        log.info("=============初始化 RabbitTemplate====================");
        this.rabbitTemplate = rabbitTemplate;
    }
    private JmsTemplate jmsTemplate;
    @Autowired
    @Profile("jms")
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        log.info("=============初始化 JmsTemplate====================");
        this.jmsTemplate = jmsTemplate;
    }
    private javax.jms.Destination logQueue;
    @Autowired
    @Profile("jms")
    public void setLogQueue(Destination logQueue) {
        this.logQueue = logQueue;
    }

    @Override
    public void kafkaSend() {
        kafaka.send("kafka-topic", map.toString());
    }

    @Override
    public void kafkaSendDefault() {
        kafaka.sendDefault(map.toString());
    }

    @Override
    public void rabbitSend() {
        rabbitTemplate.convertAndSend(map.toString());
    }

    @Override
    public String jmsSendDefault() {
        String mapStr = map.toString();
        jmsTemplate.send(session -> session.createTextMessage(mapStr));
        return mapStr;
    }
    @Override
    public String jmsSendLog() {
        String mapStr = map.toString();
        jmsTemplate.send(logQueue, session -> session.createTextMessage(mapStr));
        return mapStr;
    }
}
