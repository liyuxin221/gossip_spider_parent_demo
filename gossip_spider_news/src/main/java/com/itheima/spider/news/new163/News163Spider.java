package com.itheima.spider.news.new163;

import com.google.gson.Gson;
import com.itheima.spider.news.dao.NewsDao;
import com.itheima.spider.news.pojo.News;
import com.itheima.spider.news.utils.HttpClientUtils;
import com.itheima.spider.news.utils.IdWorker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author: Liyuxin wechat:13011800146. @Title: News163Spider @ProjectName gossip_spider_parent
 * @date: 2019/1/12 15:08
 * @description: ${todo}
 */
public class News163Spider {
    private static IdWorker idWorker = new IdWorker();
    private static NewsDao newsDao = new NewsDao();

    @Test
    public void test1() throws Exception {
        // 1.确定爬取的url
        String newsUrl = "http://ent.163.com/special/000380VU/newsdata_index.js";

        // 2.获取页面信息
        String pageHtmlStr = HttpClientUtils.doGet(newsUrl);
        // 将页面返回的json串转换成List<Map<String,String>>
        String pageJson = getJsonString(pageHtmlStr);
        Gson gson = new Gson();
        List<Map<String, Object>> list = gson.fromJson(pageJson, List.class);
        for (Map<String, Object> map : list) {
            // 获取每篇文章的docurl
            String docurl = (String) map.get("docurl");

            //        跳过图集新闻
            if (docurl.contains("photoview")) {
                continue;
            }
            // 检查是否重复获取文章
            // TODO: 2019/1/12 a

            //        获取每一条新闻的信息,封装至News对象中
            String s = HttpClientUtils.doGet(docurl);
            Document document = Jsoup.parse(s);
            News news = new News();

            // 设置id
            news.setId(Long.toString(idWorker.nextId()));
            // 获取新闻标题
            news.setTitle(document.select("#epContentLeft h1").get(0).text());
            // 获取新闻链接
            news.setUrl(docurl);
            // 获取新闻时间
            String time = document.select(".post_time_source").text();
            int endIndex = time.indexOf("来源:");
            time = time.trim();
            time = time.substring(0, endIndex);
            //      System.out.println(time);
            news.setTime(time);
            //        获取新闻来源
            news.setSource(document.select("#ne_article_source").text());
            // 获取新闻编辑
            news.setEditor(document.select(".ep-editor").text());
            // 获取新闻内容
            String content = "";
            Elements elements = document.select("#endText p[class!='f_center']");
            for (Element element : elements) {
                // 拼接新闻内容
                content += element.text();
            }
//      System.out.println(content);
            news.setContent(content);

            //      将新闻保存入数据库
            newsDao.saveNews(news);
        }
    }

    private String getJsonString(String pageHtmlStr) {
        int starIndex = pageHtmlStr.indexOf("(") + 1;
        int endIndex = pageHtmlStr.lastIndexOf(")");
        pageHtmlStr = pageHtmlStr.substring(starIndex, endIndex);
        return pageHtmlStr;
    }
}
