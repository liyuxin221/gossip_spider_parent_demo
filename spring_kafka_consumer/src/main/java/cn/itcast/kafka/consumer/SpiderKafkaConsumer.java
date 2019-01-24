package cn.itcast.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author: Liyuxin wechat:13011800146.
 * @Title: consumer
 * @ProjectName gossip_spider_parent
 * @date: 2019/1/24 20:34
 * @description: 消息监听者 用来做消费者
 */
@Component
public class SpiderKafkaConsumer implements MessageListener<String, String> {

    /**
     * 监听kafka上的数据 有就消费
     *
     * @param data
     */
    public void onMessage(ConsumerRecord<String, String> data) {
        System.out.println(data.value());
    }
}
