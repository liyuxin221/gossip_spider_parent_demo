package com.itheima.spider.news.version;

import com.google.gson.Gson;
import com.itheima.spider.news.constant.SpiderConstant;
import com.itheima.spider.news.pojo.News;
import com.itheima.spider.news.utils.HttpClientUtils;
import com.itheima.spider.news.utils.IdWorker;
import com.itheima.spider.news.utils.JedisUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;

/**
 * @author: Liyuxin wechat:13011800146. @Title: News163Slave @ProjectName gossip_spider_parent
 * @date: 2019/1/14 21:48
 * @description: 获取url, 解析新闻详情页, 封装news对象
 */
public class News163Slave {

    /**
     * 分布式id生成器: 0---31之间的参数
     */
    private static IdWorker idWorker = new IdWorker(0, 0);
    /**
     * Gson对象
     */
    private static Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        while (true) {
            // 获取url
            Jedis jedis = JedisUtils.getJedis();
            List<String> urlList = jedis.brpop(20, SpiderConstant.SPIDER_NEWS_URLLIST);
            jedis.close();

            if (urlList == null || urlList.size() <= 0) {
                break;
            }

            // 获取新闻详情页
            // 对每一个url,获取新闻详情页,封装成news对象,装入newsList中
            String url = urlList.get(1);
            String s = HttpClientUtils.doGet(url);
            Document document = Jsoup.parse(s);
            News news = new News();

            // 封装news对象
            // 设置id
            news.setId(Long.toString(idWorker.nextId()));
            // 获取新闻标题
            news.setTitle(document.select("#epContentLeft h1").get(0).text());
            // 获取新闻链接
            news.setUrl(url);
            // 获取新闻时间
            String time = document.select(".post_time_source").text();
            int endIndex = time.indexOf("来源:");
            time = time.trim();
            time = time.substring(0, endIndex);
            news.setTime(time);
            // 获取新闻来源
            news.setSource(document.select("#ne_article_source").text());
            // 获取新闻编辑
            news.setEditor(document.select(".ep-editor").text());
            // 获取新闻内容
            String content = document.select("#endText p[class!='f_center']").text();
            news.setContent(content);

            // 将对象存入newsList中
            String newsJson = gson.toJson(news);
            pushNewsToList(newsJson);
        }

    }

    /**
     * 将News对象存入队列中
     *
     * @param newsJson
     */
    private static void pushNewsToList(String newsJson) {
        Jedis jedis = JedisUtils.getJedis();
        jedis.lpush(SpiderConstant.SPIDER_NEWS_NEWSLIST, newsJson);
        jedis.close();
    }
}
