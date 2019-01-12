package com.itheima.spider.anli;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 自动获取 起点中文网下的 贞观太上皇 小说下的的章节,并将章节序列化至磁盘.
 *
 * @author: Liyuxin wechat:13011800146.
 * @date: 2019/1/9
 * @description:
 */
public class QiDian {

  @Test
  public void test1() throws IOException {
    //      1.确定爬取的url
    String url = "https://read.qidian.com/chapter/svUETOWbl6fnFF_K_BxgEA2/AG8BYOCohM62uJcMpdsVgA2";

    while (url != null) {
      String artAndNext = getArtAndNext(url);
      url = artAndNext;
    }
    //      2.创建httpClient对象,发送请求
  }

  public String getArtAndNext(String url) throws IOException {
    String htmlStr = getDocumentStr(url);

    //      5.jsoup的选择器进行解析
    Document document = Jsoup.parse(htmlStr);

    //      获取章节段落
    Element titleElement = document.select(".j_chapterName").get(0);
    String titleStr = titleElement.text();
    System.out.println("章节:" + titleStr);
    System.out.println("--------------");

    //    获取章节内容
    Elements pElements = document.select("[class='read-content j_readContent'] p");

    makeArticalSerialized(titleStr, pElements);

    //    获取下一章节的连接地址

    Elements select = document.select("#j_chapterNext");

    //    获取失败退出程序
    if (select == null) {
      return null;
    }
    String nextHref = select.get(0).attr("href");
    return nextHref = "http:" + nextHref;
  }

  /**
   * 将章节内容序列化至磁盘
   *
   * @param titleStr 章节名称,同时是文件名称
   * @param pElements 文章内容
   * @throws IOException
   */
  public void makeArticalSerialized(String titleStr, Elements pElements) throws IOException {
    BufferedWriter bw = new BufferedWriter(new FileWriter(titleStr + ".txt", true));
    String line = null;

    for (Element pElement : pElements) {
      // 获取每一个段落
      //    将获取到的内容写入文件中
      line = pElement.text();
      bw.write(line);
      bw.newLine();
      bw.flush();
    }

    bw.close();
  }

  /**
   * 根据连接地址获取String类型的Document
   *
   * @param url
   * @return
   * @throws IOException
   */
  public String getDocumentStr(String url) throws IOException {
    CloseableHttpClient client = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(url);
    httpGet.setHeader(
        "User-Agent",
        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.6788.400 QQBrowser/10.3.2767.400");

    //      3.获取响应,将响应体中的html字符串保存
    CloseableHttpResponse response = client.execute(httpGet);

    //      4.jsoup将html页面转换成document对象
    HttpEntity entity = response.getEntity();
    return EntityUtils.toString(entity, "utf-8");
  }
}
