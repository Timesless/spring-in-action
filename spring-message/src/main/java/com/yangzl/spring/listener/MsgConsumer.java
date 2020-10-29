package com.yangzl.spring.springinaction.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;


/**
 * @Author yangzl
 * @Date: 2020/8/27 15:35
 * @Desc:
 * KafkaTemplate 未提供拉取消息的方法，只能通过@KafkaListener实现推送消息
 * JmsTempalte，@JmsListener
 * RabbitTemplate，@RabbitListener
 */

@Slf4j
@Component
public class MsgConsumer {

    private JmsTemplate jmsTemplate;
    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
    private Destination logQueue;
    @Autowired
    public void setLogQueue(Destination logQueue) {
        this.logQueue = logQueue;
    }

    /**
     * 2020/8/27 当前这两个方法只能触发一个
     * @param payload 载荷
     * @param consumerRecord 消息的其它元数据，可接收ConsumerRecord | Message 对象
     * @return
     */
    @KafkaListener(topics ="kafka-topic", groupId = "yangzl")
    public void handleKafkaMsg(String payload, ConsumerRecord<String, String> consumerRecord) {

        log.info("****************************");
        log.info("received from partition {} with timestamp {}",
                consumerRecord.partition(), consumerRecord.timestamp());
        log.info("payload is {}, header is {}", payload, consumerRecord.headers());
    }
    @KafkaListener(topics ="kafka-topic", groupId = "yangzl")
    public void handleKafkaMsg(String msg) {
        log.info("****************************");
        log.info("payload is {}", msg);
    }

    /**
     * 2020/8/27 消费 JMS 消息
     *  1. @JmsListener
     *  2. JmsTemplate
     * @param
     * @return
     */
    @JmsListener(destination = "jms.queue.log")
    public void handJmsLogMsg(String msg) {
        log.info("************listener log****************");
        log.info("payload is {}", msg);
    }
    @JmsListener(destination = "jms.queue.default")
    public void handJmsDefaultMsg(String msg) {
        log.info("************listener default****************");
        log.info("payload is {}", msg);
    }

    public String  pullDefaultMsg() {
        log.info("************tempalte default****************");
        Message receive = jmsTemplate.receive();
        String body = null;
        try {
            receive.getBody(String.class);
        } catch (JMSException e) {
            log.error("消息接收错误...");
        }
        return body;
    }

    public String pullLogMsg() throws  JmsException {
        log.info("************tempalte log****************");
        Message receive = jmsTemplate.receive(logQueue);
        return receive.toString();
    }

}
