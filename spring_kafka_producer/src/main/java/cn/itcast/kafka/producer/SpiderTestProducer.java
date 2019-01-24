package cn.itcast.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author: Liyuxin wechat:13011800146. @Title: SpiderTestProducer @ProjectName gossip_spider_parent
 * @date: 2019/1/24 20:15
 * @description: 新闻的生产者
 */
@Component
public class SpiderTestProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送消息到kafka
     *
     * @param data 要发送的消息数据
     */
    public void send(String data) {
        // 获取配置文件中的默认topic
        String defaultTopic = kafkaTemplate.getDefaultTopic();
        // 发送消息
        kafkaTemplate.send(defaultTopic, data);
    }
}
