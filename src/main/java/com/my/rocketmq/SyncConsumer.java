package com.my.rocketmq;

import java.util.Properties;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;

public class SyncConsumer {

	public static void main(String[] args) {
		Properties properties = new Properties();
        properties.put(PropertyKeyConst.ConsumerId, "CID_WYX_DEMO_01");// 您在MQ控制台创建的Consumer ID
        properties.put(PropertyKeyConst.AccessKey, "LTAIqIU8gY6mwKKn");// 鉴权用AccessKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, "70oFaKsS68FUWmOJCUVUUX3XsMPEwK");// 鉴权用SecretKey，在阿里云服务器管理控制台创建
        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe("wuyuanxin_demo_topic", "*", new MessageListener() {
            public Action consume(Message message, ConsumeContext context) {
                System.out.println("Receive: " + message);
                System.out.println("Receive messageBody: " + new String(message.getBody()));
                return Action.CommitMessage;
            }
        });
        consumer.start();
        System.out.println("Consumer Started");

	}

}
