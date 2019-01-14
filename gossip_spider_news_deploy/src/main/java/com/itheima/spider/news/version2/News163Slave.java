package com.itheima.spider.news.version2;

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
 * @author itheima
 * @Title: News163Slave
 * @ProjectName gossip_spider_parent
 * @Description: 获取要爬取的url(redis), 发送请求，获取html页面，解析html页面成News对象，保存News对象(json字符串)到redis的list集合中
 * @date 2019/1/1410:57
 */
public class News163Slave {


    /**
     * 分布式id生成器: 0---31之间的参数
     */
    private static IdWorker idWorker = new IdWorker(0, 0);

    /**
     * 对象和json转换
     */
    private static Gson gson = new Gson();


    public static void main(String[] args) throws IOException {

        while (true) {
            //1.获取要爬取的url(redis)
            Jedis jedis = JedisUtils.getJedis();
            // 如果有多个程序一起到redis中, 获取数据, 保证每一个程序拿到的都是唯一的:
            // redis中:rpop不是一个线程安全的方法, 当多个程序一起获取的时候, 就会出现重复获取的问题
            // 解析方法, 使用redis提供的一个阻塞的方法, 帮助解决
            // 如何使用阻塞的方法:返回值是一个list集合, list集合中只会有二个值, 第一个值表示的key  ,第二值表示的当前弹出的元素
            List<String> urlList = jedis.brpop(20, SpiderConstant.SPIDER_NEWS_URLLIST);
            jedis.close();

            //跳出循环的条件
            if (urlList == null || urlList.size() <= 0) {
                break;
            }

            String url = urlList.get(1);

            //2. 发送请求，获取html页面，解析html页面成News对象
            News news = parseItemNews(url);


            //3.保存News对象(json字符串)到redis的list集合中
            String newsJson = gson.toJson(news);
            jedis = JedisUtils.getJedis();
            jedis.lpush(SpiderConstant.SPIDER_NEWS_NEWSLIST, newsJson);
            jedis.close();
        }

    }


    /**
     * 解析新闻列表中的一条新闻的方法
     *
     * @param docurl ： 一条新闻的url链接
     */
    private static News parseItemNews(String docurl) throws IOException {

        News news = new News();

        //1. 获取每一条新闻数据的url

        //2. 获取每一条新闻数据的html页面
        String newsHtml = HttpClientUtils.doGet(docurl);

        //3. 转换成document对象
        Document document = Jsoup.parse(newsHtml);

        //4.解析document对象，--------- >News对象： 唯一标识id    标题  来源   时间   编辑    内容   url
        //标题
        String title = document.select("#epContentLeft h1").text();

//        System.out.println(title);
        //时间和来源
        String timeAndSource = document.select(".post_time_source").text();
        String[] strings = timeAndSource.split("　来源: ");
//        System.out.println(strings[0] + strings[1]);
        String time = strings[0];
        String source = strings[1];
        //内容
        String content = document.select("#endText p").text();
//        System.out.println(content);

        //编辑
        String text = document.select(".ep-editor").text();
        String[] editors = text.split("：");
        String editor = editors[1];
//        System.out.println(editor);

        //id
        news.setId(idWorker.nextId() + "");
        news.setUrl(docurl);
        news.setEditor(editor);
        news.setContent(content);
        news.setTime(time);
        news.setTitle(title);
        news.setSource(source);

        return news;

    }
}
