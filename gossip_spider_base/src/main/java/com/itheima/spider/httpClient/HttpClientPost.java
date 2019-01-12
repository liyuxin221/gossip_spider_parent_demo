package com.itheima.spider.httpClient;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author: Liyuxin wechat:13011800146.
 * @date: 2019/1/9
 * @description: 使用HttpClient发送post请求
 */
public class HttpClientPost {

  @Test
  public void test1() throws Exception {
    //      1.确定url
    String url = "http://www.itcast.cn";

    //      2.发送请求,获取数据
    //      2.1创建httpClient对象
    CloseableHttpClient client = HttpClients.createDefault();

    //        2.2设置请求方式
    HttpPost httpPost = new HttpPost(url);

    //        2.3 设置请求参数和请求头
    ArrayList<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();

    list.add(new BasicNameValuePair("username", "xiaochuan"));
    list.add(new BasicNameValuePair("password", "123"));

    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "utf-8");
    httpPost.setEntity(entity);

    //        2.4 发送请求,获取响应对象
    CloseableHttpResponse response = client.execute(httpPost);

    //        2.5 获取数据
    //        2.5.1获取状态码
    int statusCode = response.getStatusLine().getStatusCode();
    if (statusCode == 200) {
      HttpEntity entity1 = response.getEntity();
      System.out.println(entity1);
    }
  }
}
