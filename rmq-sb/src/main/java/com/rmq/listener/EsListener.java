package com.rmq.listener;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @author yurui
 * @date 2022-06-14 21:13
 */
@Service
@RocketMQMessageListener(consumerGroup = "search-consumer",
    topic = "goods-to-es", selectorExpression = "goods-tag",
    consumeMode = ConsumeMode.CONCURRENTLY, messageModel = MessageModel.CLUSTERING)
public class EsListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("消息：" + message + " 消费成功");
    }
}
