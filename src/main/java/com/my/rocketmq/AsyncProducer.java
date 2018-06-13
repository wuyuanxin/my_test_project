package com.my.rocketmq;

import java.util.Properties;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;

public class AsyncProducer {

	public static void main(String[] args) {
		Properties properties = new Properties();
        properties.put(PropertyKeyConst.ProducerId, "PID_YJC_DEMO_02");// 您在MQ控制台创建的Producer ID
        properties.put(PropertyKeyConst.AccessKey, "rIa4vosh93cvU79x");// 鉴权用AccessKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, "Cp3Mqo0UgqbLfRU5dSCboxLHei73M8");// 鉴权用SecretKey，在阿里云服务器管理控制台创建
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "10000");//设置发送超时时间，单位毫秒
        Producer producer = ONSFactory.createProducer(properties);
        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可。
        producer.start();
        Message msg = new Message(
                // Message Topic
                "yjc_mq_demo",
                // Message Tag,
                // 可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在MQ服务器过滤
                "demo3",
                // Message Body
                // 任何二进制形式的数据， MQ不做任何干预，
                // 需要Producer与Consumer协商好一致的序列化和反序列化方式
                "Hello MQ-Async---------------------".getBytes());
        // 设置代表消息的业务关键属性，请尽可能全局唯一，以方便您在无法正常收到消息情况下，可通过MQ控制台查询消息并补发
        // 注意：不设置也不会影响消息正常收发
        msg.setKey("demo003");
        // 异步发送消息, 发送结果通过callback返回给客户端。
        producer.sendAsync(msg, new SendCallback() {
            public void onSuccess(final SendResult sendResult) {
                // 消费发送成功
                System.out.println("send message success. topic=" + sendResult.getTopic() + ", msgId=" + sendResult.getMessageId());
            }
            public void onException(OnExceptionContext context) {
            	System.out.println("exception:" + context.getException());
                // 消息发送失败
                System.out.println("send message failed. topic=" + context.getTopic() + ", msgId=" + context.getMessageId());
            }
        });
        // 在callback返回之前即可取得msgId。
        System.out.println("send message async. topic=" + msg.getTopic() + ", msgId=" + msg.getMsgID());
        
        Message msg2 = new Message(
                // Message Topic
                "yjc_mq_demo",
                // Message Tag,
                // 可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在MQ服务器过滤
                "demo3",
                // Message Body
                // 任何二进制形式的数据， MQ不做任何干预，
                // 需要Producer与Consumer协商好一致的序列化和反序列化方式
                "Hello RocketMQ===================".getBytes());
        // 设置代表消息的业务关键属性，请尽可能全局唯一，以方便您在无法正常收到消息情况下，可通过MQ控制台查询消息并补发
        // 注意：不设置也不会影响消息正常收发
        msg2.setKey("demo003");
        // 异步发送消息, 发送结果通过callback返回给客户端。
        producer.sendAsync(msg2, new SendCallback() {
            public void onSuccess(final SendResult sendResult) {
                // 消费发送成功
                System.out.println("send message2 success. topic=" + sendResult.getTopic() + ", msgId=" + sendResult.getMessageId());
            }
            public void onException(OnExceptionContext context) {
            	System.out.println("exception2:" + context.getException());
                // 消息发送失败
                System.out.println("send message2 failed. topic=" + context.getTopic() + ", msgId=" + context.getMessageId());
            }
        });
        // 在callback返回之前即可取得msgId。
        System.out.println("send message2 async. topic=" + msg.getTopic() + ", msgId=" + msg.getMsgID());
        
        // 在应用退出前，销毁Producer对象。注意：如果不销毁也没有问题
        producer.shutdown();
	}

}
