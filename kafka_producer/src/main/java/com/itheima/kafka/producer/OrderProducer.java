package com.itheima.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Date;
import java.util.Properties;

/**
 * @author: Liyuxin wechat:13011800146. @Title: OrderProducer @ProjectName gossip_spider_parent
 * @date: 2019/1/23 20:28
 * @description: 生产者 生产订单数据
 */
public class OrderProducer {
    public static void main(String[] args) throws InterruptedException {
        // 配置连接kafka参数信息
        Properties props = new Properties();
        props.put("bootstrap.servers", "node01:9092");
        props.put("acks", "all");
        props.put("retries", 0); // 重试的次数，0不重试，如果消息发送失败，是否重新发送
        props.put(
                "batch.size", 10); // 批次，一次发送多少条 发送向同一个partition的信息,达到该条数,一次性发送给consumer,但是即使不满一个batch,也会发送
        props.put("linger.ms", 2000); // 时间间隔
        props.put("buffer.memory", 33554432); // 缓冲区的大小
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // 创建kafkaproducer
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(props);

        // 发送消息
        while (true) {

            ProducerRecord<String, String> record =
                    new ProducerRecord<String, String>("order2", 1, "sdf", "订单" + new Date());
            kafkaProducer.send(record);
            Thread.sleep(1000);
        }

    }
}
