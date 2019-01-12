package com.itheima.spider.news.Utils;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author: Liyuxin wechat:13011800146.
 * @date: 2019/1/11
 * @description: httpclient的工具类
 */
public class HttpClientUtils {
    private static PoolingHttpClientConnectionManager connectionManager;

    static {
//      定义一个连接池的工具类对象
        connectionManager = new PoolingHttpClientConnectionManager();
//    定义连接池属性
//    定义连接池最大的连接数
        connectionManager.setMaxTotal(200);
//    定义主机的最大并发数
        connectionManager.setDefaultMaxPerRoute(20);
    }

//  获取close
}

