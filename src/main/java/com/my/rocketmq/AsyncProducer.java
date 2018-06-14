package com.my.rocketmq;

import java.util.Properties;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;

/**
 * 备注》》
 * 1.登录阿里云，然后创建Access Key
 * 创建地址：https://ak-console.aliyun.com/?spm=5176.2020520142.aliyun_topbar.193.50832b56hnCJ66#/
 * 
 * 2.开通消息队列（MQ）
 * 开通地址：https://www.aliyun.com/product/ons
 * 
 * 3.创建Topic/生产者/消费者
 * 地址：https://ons.console.aliyun.com/?spm=5176.6660585.774526198.1.7c9f6bf8ovoX8B#/TopicManagement?regionId=mq-internet-access&_k=tgn66m
 * 
 * @author wyx
 *
 */
public class AsyncProducer {

	public static void main(String[] args) {
		Properties properties = new Properties();
        properties.put(PropertyKeyConst.ProducerId, "PID_WYX_DEMO_01");// 您在MQ控制台创建的Producer ID
        properties.put(PropertyKeyConst.AccessKey, "LTAIqIU8gY6mwKKn");// 鉴权用AccessKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, "70oFaKsS68FUWmOJCUVUUX3XsMPEwK");// 鉴权用SecretKey，在阿里云服务器管理控制台创建
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "10000");//设置发送超时时间，单位毫秒
        Producer producer = ONSFactory.createProducer(properties);
        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可。
        producer.start();
        for (int t = 0 ; t < 50; t++) {
        	Message msg = new Message(
                    // Message Topic
                    "wuyuanxin_demo_topic",
                    // Message Tag,
                    // 可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在MQ服务器过滤
                    "demo3",
                    // Message Body
                    // 任何二进制形式的数据， MQ不做任何干预，
                    // 需要Producer与Consumer协商好一致的序列化和反序列化方式
                    ("Hello MQ-Async---------------------"+ (t + 1)).getBytes());
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
        }
        // 在应用退出前，销毁Producer对象。注意：如果不销毁也没有问题
        producer.shutdown();
	}

}
