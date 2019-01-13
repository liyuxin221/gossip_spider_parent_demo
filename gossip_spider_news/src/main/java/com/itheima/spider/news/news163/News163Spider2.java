package com.itheima.spider.news.news163;

import com.google.gson.Gson;
import com.itheima.spider.news.constant.SpiderConstant;
import com.itheima.spider.news.dao.NewsDao;
import com.itheima.spider.news.pojo.News;
import com.itheima.spider.news.utils.HttpClientUtils;
import com.itheima.spider.news.utils.IdWorker;
import com.itheima.spider.news.utils.JedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author itheima
 * @Title: News163Spider
 * @ProjectName gossip_spider_parent
 * @Description: 163娱乐新闻爬取类
 * @date 2019/1/119:58
 */
public class News163Spider2 {


    /**
     * 分布式id生成器: 0---31之间的参数
     */
    private static IdWorker idWorker = new IdWorker(0, 0);


    /**
     * 新闻的dao
     */
    private static NewsDao newsDao = new NewsDao();

    public static void main(String[] args) throws IOException {

        //1. 确定获取新闻列表的url：http://ent.163.com/special/000380VU/newsdata_index.js
        String url = "http://ent.163.com/special/000380VU/newsdata_index.js";

        //2. 调用httpClient工具，获取新闻列表json数据
//        String jsonNews = HttpClientUtils.doGet(url);


        //3.解析新闻数据
//        parseJsonNews(jsonNews);

        //调用分页爬取的方法
        page163(url);

    }

    /**
     * 分页爬取163新闻的方法
     *
     * @param indexUrl 首页url
     * @throws IOException
     */
    public static void page163(String indexUrl) throws IOException {
        //首页的url
        String url = indexUrl;

        int page = 2;
        while (true) {
            //先爬取第一页数据
            String jsonNews = HttpClientUtils.doGet(url);
            if (StringUtils.isEmpty(jsonNews)) {
                //没有数据跳出循环
                break;
            }
            parseJsonNews(jsonNews);

            //构造下一页的url
            String pageString = "";
            if (page < 10) {
                pageString = "0" + page;
            } else {
                pageString = page + "";
            }
            page++;
            url = "http://ent.163.com/special/000380VU/newsdata_index_" + pageString + ".js";
            //循环爬取下一页
        }

    }


    /**
     * 解析新闻数据
     *
     * @param jsonNews
     */
    private static void parseJsonNews(String jsonNews) throws IOException {

        //1. 处理json字符串，转换成格式良好的json数组
        jsonNews = getJsonString(jsonNews);
//        System.out.println(jsonNews);

        //2. 遍历json数组 ： json字符串------->集合对象
        Gson gson = new Gson();
        List<Map<String, Object>> newsList = gson.fromJson(jsonNews, List.class);

        for (Map<String, Object> newsString : newsList) {
            String docurl = (String) newsString.get("docurl");
            //过滤图集的url
            if (docurl.contains("photoview")) {
                continue;
            }

            //过滤已经爬取过的url(redis中的set集合进行判断)
            boolean hasParsed = hasParsedUrl(docurl);
            if (hasParsed) {
                //已经爬取过了
                continue;
            }

            System.out.println("新闻的url：" + docurl);

            //3.解析一条新闻的url --  News
            News news = parseItemNews(docurl);
            System.out.println(news);

            //4. 保存新闻数据
            newsDao.saveNews(news);


            //5. 将爬取过的新闻url保存到redis的set集合中
            saveDocUrlToRedis(docurl);

        }

    }

    /**
     * 判断给定的url是否已经爬取过
     *
     * @param docurl 判断的url
     * @return true  已经爬取，false 未爬取
     */
    private static boolean hasParsedUrl(String docurl) {
        //获取redis链接
        Jedis jedis = JedisUtils.getJedis();
        //判断给定url是否已经在redis的set集合中
        Boolean sismember = jedis.sismember(SpiderConstant.SPIDER_NEWS163, docurl);
        jedis.close();
        return sismember;
    }

    /**
     * 将已经爬取过的新闻的url保存到redis中
     *
     * @param docurl
     */
    private static void saveDocUrlToRedis(String docurl) {
        Jedis jedis = JedisUtils.getJedis();
        //将给定的url保存到redis的set集合中
        jedis.sadd(SpiderConstant.SPIDER_NEWS163, docurl);
        //用完即释放
        jedis.close();

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

    /**
     * 处理json数据的方法
     *
     * @param jsonNews
     * @return 处理完格式良好的json字符串
     */
    private static String getJsonString(String jsonNews) {
        int startIndex = jsonNews.indexOf("(");
        int lastIndex = jsonNews.lastIndexOf(")");
        jsonNews = jsonNews.substring(startIndex + 1, lastIndex);
        return jsonNews;
    }
}
