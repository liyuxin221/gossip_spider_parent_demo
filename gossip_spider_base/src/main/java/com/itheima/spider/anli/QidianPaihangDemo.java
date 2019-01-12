package com.itheima.spider.anli;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

/**
 * 爬取起点中文网排行榜的十本书.
 *
 * @author: Liyuxin wechat:13011800146.
 * @date: 2019/1/11
 * @description: 爬取起点中文网排行榜的十本书.
 */
public class QidianPaihangDemo {

    @Test
    public void test01() throws IOException {
        //        1.确定url
        String url = "https://www.qidian.com/";

        //2.获取document对象
        Document document = getDocument(url);
        Elements elements =
                document.select("#rank-list-row .rank-list[data-l2='3'] a[href*=info][class!=link]");

        for (Element element : elements) {
            // 循环遍历每个网页地址
            System.out.println(element.text() + "\t" + "http:" + element.attr("href"));
            System.out.println("-------------");
        }
    }

    /**
     * 根据url获取网页document对象
     *
     * @param url
     * @return
     * @throws IOException
     */
    private Document getDocument(String url) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader(
                new BasicHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0"));
        CloseableHttpResponse response = client.execute(httpGet);
        String s = EntityUtils.toString(response.getEntity(), "utf-8");
        return Jsoup.parse(s);
    }
}
