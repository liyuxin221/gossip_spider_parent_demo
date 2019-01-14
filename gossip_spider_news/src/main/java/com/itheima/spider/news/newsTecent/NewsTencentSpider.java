package com.itheima.spider.news.newsTecent;

import com.google.gson.Gson;
import com.itheima.spider.news.constant.SpiderConstant;
import com.itheima.spider.news.dao.NewsDao;
import com.itheima.spider.news.pojo.News;
import com.itheima.spider.news.utils.HttpClientUtils;
import com.itheima.spider.news.utils.IdWorker;
import com.itheima.spider.news.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Liyuxin wechat:13011800146. @Title: NewsTencentSpider @ProjectName gossip_spider_parent
 * @date: 2019/1/13 19:17
 * @description: 爬取腾讯娱乐新闻
 */
public class NewsTencentSpider {
    /**
     * 分布式id生成器: 0---31之间的参数
     */
    private static IdWorker idWorker = new IdWorker(0, 0);
    /**
     * 存储新闻信息的dao
     */
    private static NewsDao newsDao = new NewsDao();
    /**
     * Gson对象
     */
    private static Gson gson = new Gson();
    /**
     * Jedis对象
     */
    private static Jedis jedis = JedisUtils.getJedis();

    // 爬取新闻的测试方法
    public static void main(String[] args) throws Exception {
        // 1. 确定url
        String hotNewsUrl =
                "https://pacaio.match.qq.com/irs/rcd?cid=137&token=d0f13d594edfc180f5bf6b845456f3ea&id=&ext=ent&num=300";
        String normalNewsUrl =
                "https://pacaio.match.qq.com/irs/rcd?cid=146&token=49cbb2154853ef1a74ff4e53723372ce&ext=ent&page=0";

        // 2.爬取新闻
        parsedTecentNews(hotNewsUrl, normalNewsUrl);
    }

    private static void parsedTecentNews(String hotNewsUrl, String normalNewsUrl) throws IOException {
        // 1.爬取热点新闻
        String hotNews = HttpClientUtils.doGet(hotNewsUrl);
        List<News> hotNewsList = parseNewsJson(hotNews);
        saveNews(hotNewsList);

        // 2.爬取非热点新闻
        int page = 1;
        while (true) {
            // 解析json数据--->List<News>
            String normalNews = HttpClientUtils.doGet(normalNewsUrl);
            List<News> normalNewsList = parseNewsJson(normalNews);

            // 判断当前页是否存在
            if (normalNewsList == null || normalNewsList.size() <= 0) {
                break;
            }
            // 保存数据到数据库
            saveNews(normalNewsList);

            // 拼接下一页的url
            normalNewsUrl =
                    "https://pacaio.match.qq.com/irs/rcd?cid=146&token=49cbb2154853ef1a74ff4e53723372ce&ext=ent&page="
                            + page;
            page++;
        }

        jedis.close();
    }

    /**
     * 解析json数据,转换成List<News>格式
     *
     * @param hotNews json类型的新闻数据
     * @return List<News>
     */
    private static List<News> parseNewsJson(String hotNews) {
        // 1.json数据转换成Map<String,Object>
        Map<String, Object> map = gson.fromJson(hotNews, Map.class);
        // 2.Map<String,Object>--->List<Map<String,Object>>
        List<Map<String, Object>> data = (List<Map<String, Object>>) map.get("data");
        // 3.遍历新闻列表 封装新闻数据(List<News>)
        List<News> newsList = new ArrayList<News>();
        for (Map<String, Object> newsMap : data) {
            // 3.1过滤视频类型
            String url = (String) newsMap.get("url");
            if (url.contains("video")) {
                continue;
            }

            // 3.2判断url是否爬取过
            if (hasParsedUrl(url)) {
                continue;
            }

            News news = new News();
            // id
            news.setId(Long.toString(idWorker.nextId()));
            // 标题
            news.setTitle((String) newsMap.get("title"));
            // 链接
            news.setUrl(url);
            // 时间
            news.setTime((String) newsMap.get("update_time"));
            // 来源
            news.setSource((String) newsMap.get("source"));
            // 编辑
            news.setEditor((String) newsMap.get("source"));
            // 内容
            news.setContent((String) newsMap.get("intro"));
            // 将news对象添加入List中
            newsList.add(news);
        }
        return newsList;
    }

    /**
     * 判断url是否已被爬取
     *
     * @param docurl
     * @return true 已经爬取 ,false 未爬取
     */
    private static boolean hasParsedUrl(String docurl) {
        Boolean sismember = jedis.sismember(SpiderConstant.SPIDER_NEWS163, docurl);
        return sismember;
    }

    /**
     * 将新闻列表保存至数据库中
     *
     * @param hotNewsList 新闻列表
     */
    private static void saveNews(List<News> hotNewsList) {
        for (News news : hotNewsList) {
            newsDao.saveNews(news);
            // 将url保存至缓存
            saveNewsUrlToRedis(news.getUrl());
        }
    }

    /**
     * 将已经爬取的新闻url地址放入Redis缓存中
     *
     * @param docurl
     */
    private static void saveNewsUrlToRedis(String docurl) {
        jedis.sadd(SpiderConstant.SPIDER_NEWS_TENCENT, docurl);
  }
}
