package com.itheima.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author: Liyuxin wechat:13011800146.
 * @date: 2019/1/9
 * @description: Jedis工具类
 */
public class JedisUtils {
  private static final String host = "192.168.72.142";
  private static final int port = 6379;
  private static JedisPool jedisPool = null;

  static {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxIdle(20);
    jedisPoolConfig.setMaxTotal(100);
    jedisPool = new JedisPool(jedisPoolConfig, host, port);
  }

  /**
   * 获取jedis连接对象,用完要关闭close
   *
   * @return
   */
  public static Jedis getJedis() {
    return jedisPool.getResource();
  }
}
