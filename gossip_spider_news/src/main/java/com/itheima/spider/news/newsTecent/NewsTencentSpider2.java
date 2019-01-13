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
 * @author itheima
 * @Title: NewsTencentSpider
 * @ProjectName gossip_spider_parent
 * @Description: 腾讯娱乐新闻爬虫(示范代码)
 * @date 2019/1/1114:44
 */
public class NewsTencentSpider2 {

    /**
     * json转换的对象
     */
    public static Gson gson = new Gson();
    /**
     * 分布式id生成器: 0---31之间的参数
     */
    private static IdWorker idWorker = new IdWorker(0, 0);
    /**
     * dao层对象
     */
    private static NewsDao newsDao = new NewsDao();

    public static void main(String[] args) throws IOException {
        //1.确定url:热点新闻url   非热点新闻url

        String hotUrl = "https://pacaio.match.qq.com/irs/rcd?cid=137&token=d0f13d594edfc180f5bf6b845456f3ea&id=&ext=ent&num=60";

        String nohotUrl = "https://pacaio.match.qq.com/irs/rcd?cid=146&token=49cbb2154853ef1a74ff4e53723372ce&ext=ent&page=0";


        //2.发送请求，获取新闻的json数据
        /*String hotJson = HttpClientUtils.doGet(hotUrl);
        String noHotJson = HttpClientUtils.doGet(nohotUrl);

        System.out.println("热点新闻：" + hotJson);
        System.out.println("非热点数据：" + noHotJson);*/

        //3. 解析json数据------>List<News>
        /*List<News> hotList = parseNewsJson(hotJson);
        List<News> noHotList = parseNewsJson(noHotJson);*/

        //4. 保存数据到数据库中
       /* saveNewsList(hotList);
        saveNewsList(noHotList);*/

        pageTencent(hotUrl, nohotUrl);
    }

    /**
     * 处理分页爬取的方法
     *
     * @param hotUrl   ： 热点新闻的url
     * @param nohotUrl ： 非热点新闻url
     */
    public static void pageTencent(String hotUrl, String nohotUrl) throws IOException {
        //1. 处理热点新闻（只有一页数据）
        String hotJson = HttpClientUtils.doGet(hotUrl);
        List<News> hotList = parseNewsJson(hotJson);
        saveNewsList(hotList);


        //非热点新闻爬取(带分页)
        int page = 1;
        while (true) {
            //根据url获取json数据
            String noHotJson = HttpClientUtils.doGet(nohotUrl);

            //解析json数据，转换成List<News>
            List<News> noHotList = parseNewsJson(noHotJson);

            //跳出循环的判断
            if (noHotList == null || noHotList.size() <= 0) {
                break;
            }

            //保存数据到数据库
            saveNewsList(noHotList);


            //处理下一页的url
            nohotUrl = "https://pacaio.match.qq.com/irs/rcd?cid=146&token=49cbb2154853ef1a74ff4e53723372ce&ext=ent&page=" + page;
            page++;
        }


    }


    /**
     * 解析json数据，转换成新闻列表List<News></>
     *
     * @param newsJson : json类型的新闻数据
     * @return
     */
    private static List<News> parseNewsJson(String newsJson) {
        List<News> newslist = new ArrayList<News>();

        //1. json数据转成对象
        Map<String, Object> map = gson.fromJson(newsJson, Map.class);

        //2. 获取里面的新闻列表
        List<Map<String, Object>> newsData = (List<Map<String, Object>>) map.get("data");

        //3. 遍历新闻列表，封装新闻数据(List<News>)
        for (Map<String, Object> newsMap : newsData) {
            News news = new News();
            //获取新闻的url
            String url = (String) newsMap.get("url");

            //过滤视频(视频类型的新闻不要)
            if (url.contains("video")) {
                continue;
            }

            //判断是否已经爬取过
            boolean hasParsed = hasParsedUrl(url);
            if (hasParsed) {
                continue;
            }

            //获取新闻标题
            String title = (String) newsMap.get("title");
//            System.out.println(url  + " :  " +title);

            //获取来源
            String source = (String) newsMap.get("source");
            //时间
            String time = (String) newsMap.get("update_time");
            //内容
            String content = (String) newsMap.get("intro");

            //id
            news.setId(idWorker.nextId() + "");
            news.setSource(source);
            news.setTitle(title);
            news.setTime(time);
            news.setContent(content);
            news.setEditor(source);
            news.setUrl(url);
            //将新闻对象添加到list列表中
            newslist.add(news);
        }
        System.out.println(newslist.size());

        //返回结果
        return newslist;

    }

    /**
     * 判断给定url是否已经爬取过
     *
     * @param url 新闻的url
     * @return true : 已经爬取过   false  : 未爬取
     */
    private static boolean hasParsedUrl(String url) {
        Jedis jedis = JedisUtils.getJedis();
        //判断给定的url是否在腾讯url的set集合中
        Boolean sismember = jedis.sismember(SpiderConstant.SPIDER_NEWS_TENCENT, url);
        jedis.close();
        return sismember;

    }

    /**
     * 将新闻列表数据保存到数据库中
     *
     * @param newsList ： 新闻列表
     */
    private static void saveNewsList(List<News> newsList) {
        for (News news : newsList) {
            newsDao.saveNews(news);
            //将已经爬取过的新闻，保存到redis的set集合中
            Jedis jedis = JedisUtils.getJedis();
            //将这条新闻的url保存到redis中
            jedis.sadd(SpiderConstant.SPIDER_NEWS_TENCENT, news.getUrl());
            jedis.close();
        }


    }
}
