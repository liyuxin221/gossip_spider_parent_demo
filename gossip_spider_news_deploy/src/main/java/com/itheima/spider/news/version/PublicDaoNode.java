package com.itheima.spider.news.version;

import com.google.gson.Gson;
import com.itheima.spider.news.constant.SpiderConstant;
import com.itheima.spider.news.dao.NewsDao;
import com.itheima.spider.news.kafka.NewsProducer;
import com.itheima.spider.news.pojo.News;
import com.itheima.spider.news.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author: Liyuxin wechat:13011800146. @Title: PublicNewsDao @ProjectName gossip_spider_parent
 * @date: 2019/1/14 21:49
 * @description: 根据Json格式的news对象, 保存数据
 */
public class PublicDaoNode {
    /**
     * 存储新闻信息的dao
     */
    private static NewsDao newsDao = new NewsDao();
    /**
     * Gson对象
     */
    private static Gson gson = new Gson();

    /**
     * 新闻生产者
     */
    private static NewsProducer newsProducer = new NewsProducer();


    public static void main(String[] args) {
        while (true) {
            // 从newsList获取news对象
            List<String> newsJsonList = getJsonNews();
            if (newsJsonList == null || newsJsonList.size() <= 0) {
                break;
            }

            String newsJson = newsJsonList.get(1);
            News news = gson.fromJson(newsJson, News.class);

            // 判断该页面是否已经爬取
            String url = news.getUrl();
            boolean flag = hasParsedUrl(url);
            if (flag) {
                continue;
            }


            //保存news对象进入数据库
            newsDao.saveNews(news);

            //4.调用生产者代码,将news字符串发送到kafka
            newsProducer.send(newsJson);

            //将url放入去重集合urlSet
            saveNewsUrlToRedis(url);

        }

    }

    /**
     * 从newsList获取news对象
     *
     * @return newsJson
     */
    private static List<String> getJsonNews() {
        Jedis jedis = JedisUtils.getJedis();
        List<String> newsJson = jedis.brpop(20, SpiderConstant.SPIDER_NEWS_NEWSLIST);
        jedis.close();
        return newsJson;
    }

    /**
     * @param docurl
     * @return true 已经爬取 ,false 未爬取
     */
    public static boolean hasParsedUrl(String docurl) {
        Jedis jedis = JedisUtils.getJedis();
        Boolean sismember = jedis.sismember(SpiderConstant.SPIDER_NEWS_URLSET, docurl);
        jedis.close();
        return sismember;
    }

    /**
     * 将已经爬取的新闻url地址放入urlSet
     *
     * @param url
     */
    private static void saveNewsUrlToRedis(String url) {
        Jedis jedis = JedisUtils.getJedis();
        jedis.sadd(SpiderConstant.SPIDER_NEWS_URLSET, url);
        jedis.close();
    }

}
