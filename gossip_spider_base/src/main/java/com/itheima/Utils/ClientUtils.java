package com.itheima.Utils;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author: Liyuxin wechat:13011800146.
 * @date: 2019/1/10
 * @description: ${todo}
 */
public class ClientUtils {

    private static String urlDir = null;

    private ClientUtils() {
    }

    /**
     * 将页面html序列化至磁盘
     *
     * @param titleStr  章节名称,同时是文件名称
     * @param pElements 文章内容
     * @throws IOException
     */
    public static void allHtmlSerialized(String titleStr, Elements pElements) throws IOException {
        //        拼接保存路径
        //        if(urlDir==)
        //        String savePath=u
        BufferedWriter bw = new BufferedWriter(new FileWriter(titleStr + ".txt", true));
        String line = null;

        for (Element pElement : pElements) {
            // 获取每一个段落
            int i = 0;
            //    将获取到的内容写入文件中
            line = pElement.text();
            bw.write(line);
            bw.newLine();
            bw.flush();
        }

        bw.close();
    }
}
