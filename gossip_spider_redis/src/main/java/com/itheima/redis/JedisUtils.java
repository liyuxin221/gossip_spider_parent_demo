package com.itheima.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.swing.*;

/**
 * @author: Liyuxin wechat:13011800146.
 * @date: 2019/1/9
 * @description: ${todo}
 */
public class JedisUtils {
  private static JedisPool jedisPool = null;
  private static final String host = "192.168.72.142";
  private static final int port = 6379;

  static {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxIdle(20);
    jedisPoolConfig.setMaxTotal(100);
    jedisPool = new JedisPool(jedisPoolConfig, host, port);
  }

    /**
     * 获取jedis连接对象,用完要关闭close
     * @return
     */
  public static Jedis getJedis() {
    return jedisPool.getResource();
  }

}
