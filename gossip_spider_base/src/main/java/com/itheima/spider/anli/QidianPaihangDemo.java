package com.itheima.spider.anli;

import com.sun.istack.internal.NotNull;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

        // 2.获取document对象
        Document document = getDocument(url);
        // 获取到每一本书的书名和url
        Elements elements =
                document.select("#rank-list-row .rank-list[data-l2='3'] a[href*=info][class!=link]");
        int bookSerial = 1;

        for (Element element : elements) {
            // 书名
            String bookName = element.text();
            bookName = wipeIllegalPathCharacter(bookName);
            // 在保存目录创建书名命名的文件夹
            String bookPath = "D:\\QMDownload\\QiDianDownLoad\\" + bookSerial + " " + bookName + "\\";
            bookSerial++;
            new File(bookPath).mkdir();
            // 书的试读连接地址
            String bookTestUrl = "http:" + element.attr("href");

            // 3.进入info页面
            Document bTDocument = getDocument(bookTestUrl);
            // 获取免费试读连接
            String readUrl = "http:" + bTDocument.select("#readBtn").get(0).attr("href");

            // 4.进入试读阅读页面
            Document testReadPageDocument = getDocument(readUrl);

            // 5.下载小说
            // 获取书的章节名称
            String sectionName = testReadPageDocument.select(".j_chapterName").text();
            sectionName = wipeIllegalPathCharacter(sectionName);

            // 获取书的段落
            Elements paragraphs = testReadPageDocument.select(".read-content j_readContent");
            // 获取书的章节内容,并序列化至磁盘
            BufferedWriter fileWriter =
                    new BufferedWriter(new FileWriter(bookPath + "\\" + sectionName + ".txt", true));
            for (Element paragraph : paragraphs) {
                // 对每一个段落写入文件
                fileWriter.write(paragraph.text());
                fileWriter.newLine();
                fileWriter.flush();
            }
            // 关闭写入资源
            fileWriter.close();
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

    /**
     * 将文件名中的非法字符用空格代替
     *
     * @param Path
     * @return 去掉非法字符后的文件名
     */
    private String wipeIllegalPathCharacter(@NotNull String Path) {
        if (Path == null || Path.length() <= 0) {
            return null;
        }
        String[] illegalCharacters = {"？", "[|]", "[*]", "[|]", "[“]", "[<]", "[>]", "[:]", "[/]"};
        for (String illegalCharacter : illegalCharacters) {
            Path = Path.replaceAll(illegalCharacter, " ");
        }
        System.out.println(Path);
        return Path;
    }
}
