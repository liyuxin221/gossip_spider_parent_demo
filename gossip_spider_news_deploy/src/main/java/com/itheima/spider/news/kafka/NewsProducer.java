package com.itheima.spider.news.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

/**
 * @author: Liyuxin wechat:13011800146. @Title: NewsProducer @ProjectName gossip_spider_parent
 * @date: 2019/1/24 21:48
 * @description: 新闻的生产者
 */
public class NewsProducer {

    private static Properties properties = null;
    private static KafkaProducer<String, String> kafkaProducer = null;

    static {

    }
}
