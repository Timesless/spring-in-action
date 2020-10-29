package com.yangzl.spring.springinaction.controller;

import com.yangzl.spring.springinaction.listener.MsgConsumer;
import com.yangzl.spring.springinaction.service.MsgProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author yangzl
 * @Date: 2020/8/27 15:52
 * @Desc:
 */

@RestController
public class MQController {

    private MsgProducerService msgProducerService;
    @Autowired
    public void setMsgProducerService(MsgProducerService msgProducerService) {
        this.msgProducerService = msgProducerService;
    }
    private MsgConsumer msgConsumer;
    @Autowired
    public void setMsgConsumer(MsgConsumer msgConsumer) {
        this.msgConsumer = msgConsumer;
    }

    @GetMapping("/kafka/send")
    public void kafkaSend() {
        msgProducerService.kafkaSend();
    }

    @GetMapping("/kafka/sendDefault")
    public void kafkaSendDefault() {
        msgProducerService.kafkaSendDefault();
    }

    /**
     * 2020/8/27 本机暂时没有运行环境
     * @param
     * @return
     */
    @GetMapping("/rabbit/send")
    public void rabbitSend() { }


    @GetMapping("/jms/send")
    public String jmsSend() {
        return msgProducerService.jmsSendDefault();
    }
    /**
     * 2020/8/27 发送日志消息
     * @param
     * @return
     */
    @GetMapping("/jms/sendLog")
    public String jmsSendLog() {
        return msgProducerService.jmsSendLog();
    }

    @GetMapping("/jms/receiveDefault")
    public String receiveDefault() {
        return msgConsumer.pullDefaultMsg();
    }

    @GetMapping("jms/receiveLog")
    public String receiveLog() {
        return msgConsumer.pullLogMsg();
    }

}
