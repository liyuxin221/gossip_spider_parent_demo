package cn.itcast.kafka.producer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author: Liyuxin wechat:13011800146. @Title: SpiderTestProducerTest @ProjectName
 * gossip_spider_parent
 * @date: 2019/1/24 20:25
 * @description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:kafka_producer.xml")
public class SpiderTestProducerTest {

    @Autowired
    private SpiderTestProducer spiderTestProducer;

    @Test
    public void send() {
        spiderTestProducer.send("spring继承kafka发送消息");
    }
}