package com.itheima.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author: Liyuxin wechat:13011800146. @Title: OrderConsumer @ProjectName gossip_spider_parent
 * @date: 2019/1/23 20:27
 * @description: 消费者 消费订单数据
 */
public class OrderConsumer {

    public static void main(String[] args) throws InterruptedException {
        //配置连接kafka参数信息
        Properties props = new Properties();
        props.put("bootstrap.servers", "node01:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        //创建kafkaconsumer
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(props);
        List<String> order = Arrays.asList("order2");
        kafkaConsumer.subscribe(order);
        //消费数据
        while (true) {
            Thread.sleep(990);
            ConsumerRecords<String, String> records = kafkaConsumer.poll(1);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.value());
            }
        }

    }
}
