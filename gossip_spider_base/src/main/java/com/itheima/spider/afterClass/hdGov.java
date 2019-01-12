package com.itheima.spider.afterClass;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URLEncoder;

/**
 * @author: Liyuxin wechat:13011800146.
 * @date: 2019/1/10
 * @description:
 */
public class hdGov {
    @Test
    public void test1() throws Exception {

        //      1.确定访问url
        String url = "http://47.95.44.144/s?database=hd_qb&siteCode=1101080016&tab=all&qt=";
        //    设置搜索关键词
        String keyWord = "西三旗";
        //     将关键词添加值url中
        url = url + URLEncoder.encode(keyWord, "utf-8");
        //    System.out.println(url);

        //      2.发送请求,接收数据
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        //    2.1 设置请求头(GET方式没有请求体)

        BasicHeader[] basicHeaders = {
                new BasicHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36"),
                new BasicHeader(
                        "Cookie",
                        "SearchHistory=%25E8%25A5%25BF%25E4%25B8%2589%25E6%2597%2597%252C%25E8%25A5%25BF%25E4%25B8%2589%252C; cps=D0C900A381D7D5598A4AE451CEE8AA63; SERVERID=4d05bf5e60e007535731ac38c3d48a40|1547088756|1547088756"),
                new BasicHeader("Upgrade-Insecure-Requests", "1"),
                new BasicHeader(
                        "Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"),
                new BasicHeader("Accept-Encoding", "gzip, deflate"),
                new BasicHeader("Accept-Language", "zh-CN,zh;q=0.9"),
                new BasicHeader("Host", "47.95.44.144"),
                new BasicHeader("Connection", "keep-alive")
        };

        httpGet.setHeaders(basicHeaders);
        //    2.2 发送请求
        CloseableHttpResponse response = client.execute(httpGet);
        //      2.3 获取数据
        HttpEntity entity = response.getEntity();
        String htmlStr = EntityUtils.toString(entity, "utf-8");

        //    把网页数据序列化至磁盘
        BufferedWriter bw = new BufferedWriter(new FileWriter(keyWord + ".html", true));
        bw.write(htmlStr);
        bw.newLine();
        bw.flush();
        bw.close();

        //      3.解析页面数据
        Document document = Jsoup.parse(htmlStr);
        Element element = document.select("#Pagination > div > a.current").get(0);

        System.out.println(element.text());
    }
}
