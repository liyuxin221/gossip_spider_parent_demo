package cn.itcast.kafka.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * @author: Liyuxin wechat:13011800146.
 * @Title: SpiderKafkaConsumerTest
 * @ProjectName gossip_spider_parent
 * @date: 2019/1/24 20:51
 * @description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:kafka_consumer.xml")

public class SpiderKafkaConsumerTest {

    @Test
    public void name() throws IOException {
        System.in.read();
    }
}