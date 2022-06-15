package com.rmq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @author yurui
 * @date 2022-06-11 19:17
 */
public class ProducerTest {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException {
        // 1、创建生产者对象
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("test-producer");
        // 2、设置namesrv
        defaultMQProducer.setNamesrvAddr("localhost:9876");
        // 启动
        defaultMQProducer.start();
        // 3、设置消息的topic，从namesrv得到该信息的broker
        Message msg = new Message("test-topic", "test-tag", "hello rocketmq again! with key".getBytes());
        msg.setKeys("1234567890");
        // 4、发送消息：4.1、同步 4.2、异步回调 4.3、异步单向
//        SendResult result = defaultMQProducer.send(msg);
        defaultMQProducer.send(msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                SendStatus status = sendResult.getSendStatus();
                System.out.println("消息key：" + msg.getKeys() + "状态：" + status);
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("发送消息异常: " + e.getMessage());
            }
        });
//        defaultMQProducer.sendOneway(msg);
    }

}
