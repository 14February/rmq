package com.rmq.service.impl;

import com.rmq.service.IGoodsService;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author yurui
 * @date 2022-06-14 19:46
 */
@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Value("${topic.es}")
    private String topicName;

    @Override
    public void audit() {
        // 修改商品状态，发送消息
//        Message msg = new Message("test-topic", "test-tag", "hello spring-rocketmq!!!".getBytes());
        rocketMQTemplate.asyncSend(String.join(":", topicName, "goods-tag"), "hello spring-rocketmq!!!", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送消息成功！");
            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    @Override
    public String getMsg() {

        return null;
    }
}
