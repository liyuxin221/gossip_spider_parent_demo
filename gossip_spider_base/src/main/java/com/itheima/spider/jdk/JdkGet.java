package com.itheima.spider.jdk;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author: Liyuxin wechat:13011800146.
 * @date: 2019/1/9
 * @description: ${todo}
 */
public class JdkGet {

    @Test //课上练习
  public void test01() throws IOException {
      //    1.确定爬取的url
      String indexUrl = "http://www.itcast.cn/";

      //    2.发送请求,传递参数
      //    2.1将string类型的字符串转换成一个url对象
      URL url = new URL(indexUrl);

      //  2.2 发送请求,获取连接对象
      HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

//      2.3 设置参数:请求参数和请求方式,请求头
    urlConnection.setRequestMethod("GET");

    //    3.获取响应数据(流处理)
    //        图片视频字节流   html文档(字节流和字符流都可以处理)
        InputStream inputStream = urlConnection.getInputStream();

    int len = 0;
    byte[] bytes = new byte[1024];

//    字节流
    while ((len=inputStream.read(bytes))!=-1){
      System.out.println(new String( bytes,0,len));
    }

//    4.获取数据
  }

    @Test // 课后练习
    public void test2() {
//        1.确定爬取的url

    }
}
