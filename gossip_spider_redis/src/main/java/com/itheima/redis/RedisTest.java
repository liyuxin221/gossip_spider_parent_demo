package com.itheima.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author: Liyuxin wechat:13011800146.
 * @date: 2019/1/9
 * @description: Redis使用的测试类
 */
public class RedisTest {

  @Test
  public void jedisOne() {
    Jedis jedis = JedisUtils.getJedis();
    String ping = jedis.ping();
    System.out.println(ping);

    jedis.close();
  }
}
