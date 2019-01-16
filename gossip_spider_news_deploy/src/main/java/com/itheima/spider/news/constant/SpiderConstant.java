package com.itheima.spider.news.constant;

/**
 * @author: Liyuxin wechat:13011800146. @Title: SpiderConstant @ProjectName gossip_spider_parent
 * @date: 2019/1/13 0:21
 * @description: 定义项目中的常量, 主要为存储在缓存中数据的key值
 */
public class SpiderConstant {
    /**
     * 163新闻爬虫存放已经爬取的url的set集合的大key
     */
    public static final String SPIDER_NEWS163 = "bigData:spider:163news:docurl";

    /**
     * 腾讯娱乐新闻爬虫存放已经爬取的url的set集合的key
     */
    public static final String SPIDER_NEWS_TENCENT = "bigData:spider:newstencent:docurl";


    /**
     * bigData:spider:urlSet 存放已爬取的url的set
     */
    public static final String SPIDER_NEWS_URLSET = "bigData:spider:urlSet";

    /**
     * bigData:spider:urlList 存放url列表的list
     */
    public static final String SPIDER_NEWS_URLLIST = "bigData:spider:urlList";


    /**
     * bigData:spider:newsList 存放解析待存储进数据库的News对象(Json)形式
     */
    public static final String SPIDER_NEWS_NEWSLIST = "bigData:spider:newsList";
}
