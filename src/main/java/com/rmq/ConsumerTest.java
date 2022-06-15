package com.rmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * @author yurui
 * @date 2022-06-11 19:17
 */
public class ConsumerTest {

    public static void main(String[] args) throws MQClientException {
        // 1、创建消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-consumer");
        // 2、设置namesrv
        consumer.setNamesrvAddr("localhost:9876");
        // 3、订阅
        consumer.subscribe("test-topic", "test-tag");
        // 4、设置消费模式：集群、广播
        consumer.setMessageModel(MessageModel.CLUSTERING);
        // 设置最大重试次数为3
        consumer.setMaxReconsumeTimes(3);
        // 设置每次消费信息数<=3
        consumer.setConsumeMessageBatchMaxSize(3);
        // 5、消息逻辑处理
        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                long offset = msgs.get(0).getQueueOffset();
                String maxOffset =
                        msgs.get(0).getProperty(MessageConst.PROPERTY_MAX_OFFSET);
                long diff = Long.parseLong(maxOffset) - offset;
                if (diff > 100000) {
                    // TODO 消息堆积情况的特殊处理
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                try {
                    if (msgs != null && msgs.size() > 0) {
                        MessageExt msg = msgs.get(0);
                        String msgBody = new String(msg.getBody(), "utf-8");
                        System.out.println("消费者接受到的消息：" + msgBody);
                    }
                } catch (Exception ex) {
                    // 消费失败，补偿机制
                    // 消费3次还不成功，直接放弃
                    if (msgs.get(0).getReconsumeTimes() == 3) {
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    System.out.println("消费失败重试...");
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 启动
        consumer.start();
    }

}
