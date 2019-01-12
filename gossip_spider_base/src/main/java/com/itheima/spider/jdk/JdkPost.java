package com.itheima.spider.jdk;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author: Liyuxin wechat:13011800146.
 * @date: 2019/1/9
 * @description: 使用原始jdk方式发送post请求
 */
public class JdkPost {

  @Test
  public void test01() throws Exception {
    //        1.确定爬取的url (不能丢失http://)
    String domin = "http://www.itcast.cn";

    //    2.创建url对象,开启连接
    URL url = new URL(domin);
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

    //        2.1设置请求方式:POST
    urlConnection.setRequestMethod("POST");

    //        2.2设置开启输出模式:
    urlConnection.setDoOutput(true);

    //    3.传递数据:
    OutputStream outputStream = urlConnection.getOutputStream();
    String params = "username=zhangsan&age=123&address=xxxx";
    outputStream.write(params.getBytes());

    //    刷新缓存
    outputStream.flush();
    outputStream.close();

    //    4.获取响应数据(尝试使用字符流读取)
    InputStream inputStream = urlConnection.getInputStream();

    //    4.1使用字符流的获取方式
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

    String line = null;
    while ((line = bufferedReader.readLine()) != null) {
      System.out.println(line);
    }

    //    5.释放资源
    bufferedReader.close();
    inputStreamReader.close();
    inputStream.close();
  }
}
