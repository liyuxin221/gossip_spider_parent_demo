package com.itheima.spider.news.version;

import com.google.gson.Gson;
import com.itheima.spider.news.constant.SpiderConstant;
import com.itheima.spider.news.utils.HttpClientUtils;
import com.itheima.spider.news.utils.JedisUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: Liyuxin wechat:13011800146. @Title: News163Master @ProjectName gossip_spider_parent
 * @date: 2019/1/14 21:35
 * @description: 获取163新闻的json数据, 判断是否已经爬取, 处理下一页的url
 */
public class News163Master {

    public static void main(String[] args) throws Exception {
        // 1.确定爬取的url
        String newsUrl = "http://ent.163.com/special/000380VU/newsdata_index.js";

        // 2.获取新闻的url列表,并判断是否已经爬取
        parse163News(newsUrl);
    }

    /**
     * 获取新闻的url列表,并判断是否已经爬取
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
            // 重复
            if (hasParsedUrl(docurl)) {
                continue;
            } else {
                // 不重复,将url放入urlList
                Jedis jedis = JedisUtils.getJedis();
                jedis.lpush(SpiderConstant.SPIDER_NEWS_URLLIST, docurl);
                jedis.close();
            }
            // System.out.println("新闻的url：" + docurl);
        }
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
