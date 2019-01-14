package com.itheima.spider.news.version2;

import com.google.gson.Gson;
import com.itheima.spider.news.constant.SpiderConstant;
import com.itheima.spider.news.dao.NewsDao;
import com.itheima.spider.news.pojo.News;
import com.itheima.spider.news.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author itheima
 * @Title: PublicDaoNode
 * @ProjectName gossip_spider_parent
 * @Description: 从redis中获取news对象，(判断这个url是否已经爬取过，只有没有爬取过的才保存  )
 * 保存到数据库中，将爬取过的url写入redis的set集合中
 * @date 2019/1/1411:18
 */
public class PublicDaoNode {

    /**
     * 对象和json转换
     */
    private static Gson gson = new Gson();


    /**
     * 新闻的dao
     */
    private static NewsDao newsDao = new NewsDao();

    public static void main(String[] args) {
        while (true) {
            //1. 从redis中获取news对象
            Jedis jedis = JedisUtils.getJedis();
            List<String> newsJsonList = jedis.brpop(20, SpiderConstant.SPIDER_NEWS_NEWSLIST);
            jedis.close();
            if (newsJsonList == null || newsJsonList.size() <= 0) {
                break;
            }

            String newsJson = newsJsonList.get(1);
            News news = gson.fromJson(newsJson, News.class);
            //2. 判断是否已经爬取(url是否在set集合中)
            boolean hasParsed = hasParsedUrl(news.getUrl());
            if (hasParsed) {
                continue;
            }

            //3. 将news对象保存到数据库中
            newsDao.saveNews(news);

            //4.将爬取过的url写入redis的set集合中
            jedis = JedisUtils.getJedis();
            jedis.sadd(SpiderConstant.SPIDER_NEWS_URLSET, news.getUrl());
            jedis.close();

        }
    }

    /**
     * 判断给定的url是否已经爬取过
     *
     * @param url 新闻的url
     * @return true，已经爬取
     */
    private static boolean hasParsedUrl(String url) {
        Jedis jedis = JedisUtils.getJedis();
        Boolean sismember = jedis.sismember(SpiderConstant.SPIDER_NEWS_URLSET, url);
        jedis.close();
        return sismember;
    }
}
