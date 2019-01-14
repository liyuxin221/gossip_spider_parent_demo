package com.itheima.spider.news.version2;

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
 * @author itheima @Title: News163Master @ProjectName gossip_spider_parent @Description:
 * 获取新闻列表的json数据，判断是否已经爬取，处理下一页的url
 * @date 2019/1/1410:44
 */
public class News163Master {

    public static void main(String[] args) throws IOException {
        // 1.确定url
        String url = "http://ent.163.com/special/000380VU/newsdata_index.js";

        // 2.分页爬取新闻列表json数据
        page163(url);
    }

    /**
     * 分页爬取163新闻的方法
     *
     * @param indexUrl 首页url
     * @throws IOException
     */
    public static void page163(String indexUrl) throws IOException {
        // 首页的url
        String url = indexUrl;

        int page = 2;
        while (true) {
            // 先爬取第一页数据
            String jsonNews = HttpClientUtils.doGet(url);
            if (StringUtils.isEmpty(jsonNews)) {
                // 没有数据跳出循环
                break;
            }
            // 解析json数据
            parseJsonNews(jsonNews);

            // 构造下一页的url
            String pageString = "";
            if (page < 10) {
                pageString = "0" + page;
            } else {
                pageString = page + "";
            }
            page++;
            url = "http://ent.163.com/special/000380VU/newsdata_index_" + pageString + ".js";
            // 循环爬取下一页
        }
    }

    /**
     * 解析新闻数据
     *
     * @param jsonNews
     */
    private static void parseJsonNews(String jsonNews) throws IOException {

        // 1. 处理json字符串，转换成格式良好的json数组
        jsonNews = getJsonString(jsonNews);
        //        System.out.println(jsonNews);

        // 2. 遍历json数组 ： json字符串------->集合对象
        Gson gson = new Gson();
        List<Map<String, Object>> newsList = gson.fromJson(jsonNews, List.class);

        for (Map<String, Object> newsString : newsList) {
            String docurl = (String) newsString.get("docurl");
            // 过滤图集的url
            if (docurl.contains("photoview")) {
                continue;
            }

            // 过滤已经爬取过的url(redis中的set集合进行判断)
            boolean hasParsed = hasParsedUrl(docurl);
            if (hasParsed) {
                // 已经爬取过了
                continue;
            }

            System.out.println("新闻的url：" + docurl);

            // 将解析出来的新闻url，存放到redis的list集合中：bigData:spider:urlList（新增的代码重点）
            Jedis jedis = JedisUtils.getJedis();
            jedis.lpush(SpiderConstant.SPIDER_NEWS_URLLIST, docurl);
            jedis.close();
        }
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

    /**
     * 判断给定的url是否已经爬取过
     *
     * @param docurl 判断的url
     * @return true 已经爬取，false 未爬取
     */
    private static boolean hasParsedUrl(String docurl) {
        // 获取redis链接
        Jedis jedis = JedisUtils.getJedis();
        // 判断给定url是否已经在redis的set集合中
        Boolean sismember = jedis.sismember(SpiderConstant.SPIDER_NEWS_URLSET, docurl);
        jedis.close();
        return sismember;
    }
}
