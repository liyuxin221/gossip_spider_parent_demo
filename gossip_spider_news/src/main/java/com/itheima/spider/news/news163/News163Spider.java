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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: Liyuxin wechat:13011800146. @Title: News163Spider @ProjectName gossip_spider_parent
 * @date: 2019/1/12 15:08
 * @description: 爬取163娱乐新闻, 将每条新闻按照news形式保存至数据库
 */
public class News163Spider {
  /**
   * 分布式id生成器: 0---31之间的参数
   */
  private static IdWorker idWorker = new IdWorker(0, 0);

  /** 存储新闻信息的dao */
  private static NewsDao newsDao = new NewsDao();

    public static void main(String[] args) throws Exception {
    // 1.确定爬取的url
    String newsUrl = "http://ent.163.com/special/000380VU/newsdata_index.js";

    // 2.解析新闻数据
    parse163News(newsUrl);
  }

  /**
   * 分页爬取163新闻
   *
   * @param newsUrl
   * @throws IOException
   */
  private static void parse163News(String newsUrl) throws IOException {

    String url = newsUrl;
    int i = 2;

    while (true) {
      // 先爬取第一条数据
      String pageHtmlStr = HttpClientUtils.doGet(url);
      if (StringUtils.isEmpty(pageHtmlStr)) {
        // 如果地址为空,跳出循环
        break;
      }
      parseJasonNews(pageHtmlStr);

      // 构造下一页的url
      String pageString = "";
      if (i < 10) {
        pageString = "0" + i;
      } else {
        pageString = "" + i;
      }
      url = "http://ent.163.com/special/000380VU/newsdata_index_" + pageString + ".js";
      i++;
    }
  }

  /**
   * 根据新闻列表页面的json,解析每条新闻,并将新闻存储至数据库
   *
   * @param pageHtmlStr
   * @throws IOException
   */
  private static void parseJasonNews(String pageHtmlStr) throws IOException {
    // 将页面字符串转换成良好的json格式
    String pageJson = getJsonString(pageHtmlStr);
    Gson gson = new Gson();
    List<Map<String, Object>> list = gson.fromJson(pageJson, List.class);
    for (Map<String, Object> map : list) {
      // 获取每篇文章的docurl
      String docurl = (String) map.get("docurl");
      // 跳过图集新闻
      if (docurl.contains("photoview")) {
        continue;
      }

      // 检查是否重复获取文章
      if (hasParsedUrl(docurl)) {
        break;
      }

      // 解析每条新闻数据
      parseItemNews(docurl);

      //将已经爬取的新闻url地址放入Redis缓存中
      saveNewsUrlToRedis(docurl);
    }
  }

  /**
   * 将已经爬取的新闻url地址放入Redis缓存中
   *
   * @param docurl
   */
  private static void saveNewsUrlToRedis(String docurl) {
    Jedis jedis = JedisUtils.getJedis();
    jedis.sadd(SpiderConstant.SPIDER_NEWS163, docurl);
    jedis.close();
  }

  /**
   * @param docurl
   * @return true 已经爬取 ,false 未爬取
   */
  public static boolean hasParsedUrl(String docurl) {
    Jedis jedis = JedisUtils.getJedis();
    Boolean sismember = jedis.sismember(SpiderConstant.SPIDER_NEWS163, docurl);
    jedis.close();
    return sismember;
  }

  /**
   * 解析每条新闻数据
   *
   * @param docurl
   * @throws IOException
   */
  private static void parseItemNews(String docurl) throws IOException {
    // 获取每一条新闻的信息,封装至News对象中
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
    news.setTime(time);
    // 获取新闻来源
    news.setSource(document.select("#ne_article_source").text());
    // 获取新闻编辑
    news.setEditor(document.select(".ep-editor").text());
    // 获取新闻内容
    String content = "";
    Elements elements = document.select("#endText p[class!='f_center']");
    for (Element element : elements) {
      // 整合新闻段落
      content += element.text();
    }
    news.setContent(content);

    // 将新闻保存入数据库
    newsDao.saveNews(news);
  }

  /**
   * 将页面字符串转换成良好的json格式
   *
   * @param pageHtmlStr
   * @return
   */
  private static String getJsonString(String pageHtmlStr) {
    int starIndex = pageHtmlStr.indexOf("(") + 1;
    int endIndex = pageHtmlStr.lastIndexOf(")");
    pageHtmlStr = pageHtmlStr.substring(starIndex, endIndex);
    return pageHtmlStr;
  }
}
