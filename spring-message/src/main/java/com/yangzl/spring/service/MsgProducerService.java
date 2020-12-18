package com.yangzl.spring.springinaction.service;

/**
 * @Author yangzl
 * @Date 2020/8/27 15:19
 * @Desc
 */

public interface MsgProducerService {

    /**
     * 2020/8/27 指定topic发送
     * @param () v
     * @return ()
     */
    void kafkaSend();

    /**
     * 2020/8/27 发送至默认topic
     * @param () v
     * @return ()
     */
    void kafkaSendDefault();

    /**
     * 2020/8/27 发送RabbitMQ消息
     * @param () v
     * @return ()
     */
    void rabbitSend();

    /**
     * 2020/8/27 jms 发送默认消息
     * @param () v
     * @return ()
     */
    String jmsSendDefault();

    /**
     * 2020/8/27 jms 发送log消息
     * @param () v
     * @return ()
     */
    String jmsSendLog();
}
