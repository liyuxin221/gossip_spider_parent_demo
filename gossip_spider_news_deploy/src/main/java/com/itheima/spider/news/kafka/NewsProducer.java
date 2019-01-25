package com.itheima.spider.news.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author: Liyuxin wechat:13011800146. @Title: NewsProducer @ProjectName gossip_spider_parent
 * @date: 2019/1/24 21:48
 * @description: 新闻的生产者
 */
public class NewsProducer {

    private static Properties props = null;
    private static KafkaProducer<String, String> kafkaProducer = null;

    static {
        props = new Properties();
        props.put("bootstrap.servers", "node01:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // 1.创建KafkaProducer 也就kafka的生产者
        // 1.1 需要一个Properties对象--怎么连接kafka集群
        kafkaProducer = new KafkaProducer<String, String>(props);
    }

    public void send(String news) {
        ProducerRecord<String, String> recoder = new ProducerRecord<String, String>("news", news);
        kafkaProducer.send(recoder);
    }
}
