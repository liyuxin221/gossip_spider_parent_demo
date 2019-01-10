package com.itheima.spider.anli;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟登陆manmanbuy网站,获取页面的积分内容
 * @author: Liyuxin wechat:13011800146.
 * @date: 2019/1/9
 * @description: ${todo}
 */
public class ManManBuy {


    @Test
  public void connection() throws Exception {
//      1.确定访问地址
    String url = "http://home.manmanbuy.com/login.aspx?tourl=http%3a%2f%2fhome.manmanbuy.com%2fusercenter.aspx";

    //      2.发送请求,获取数据
    //      2.1设置请求方式
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

    //        2.2 设置请求参数

//    设置请求头
    Header[] headers={
            new BasicHeader("Referer","http://home.manmanbuy.com/login.aspx"),
            new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36"),
    };

    httpPost.setHeaders(headers);

//    设置请求体
    List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
    list.add(
        new BasicNameValuePair(
            "__VIEWSTATE",
            "/wEPDwULLTIwNjQ3Mzk2NDFkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYBBQlhdXRvTG9naW4voj01ABewCkGpFHsMsZvOn9mEZg=="));
    list.add(new BasicNameValuePair("__EVENTVALIDATION","/wEWBQLW+t7HAwLB2tiHDgLKw6LdBQKWuuO2AgKC3IeGDJ4BlQgowBQGYQvtxzS54yrOdnbC"));
    list.add(new BasicNameValuePair("txtUser","liyuxin221"));
    list.add(new BasicNameValuePair("txtPass","liyuxin920221"));
    list.add(new BasicNameValuePair("autoLogin","on"));
    list.add(new BasicNameValuePair("btnLogin", "登陆"));

        HttpEntity httpEntity = new UrlEncodedFormEntity(list,"utf-8");
        httpPost.setEntity(httpEntity);
    //        2.3 发送请求
        CloseableHttpResponse response = client.execute(httpPost);

//        2.4 获取数据
//        2.4.1 获取响应头
        int statusCode = response.getStatusLine().getStatusCode();

        if(statusCode==302){

        Header[] locations = response.getHeaders("Location");
        Header[] cookies = response.getHeaders("Set-Cookie");
        //        3.发送重定向请求
        CloseableHttpClient client1 = HttpClients.createDefault();

    //    3.1 设置请求参数
        HttpGet httpGet = new HttpGet(locations[0].getValue());

//        3.2设置请求参数
        httpGet.setHeaders(cookies);

//        3.3发送请求,获取页面信息
            CloseableHttpResponse response1 = client.execute(httpGet);
            HttpEntity entity = response1.getEntity();
            String s = EntityUtils.toString(entity, "utf-8");

      //        4.使用jsoup解析页面
            Document document = Jsoup.parse(s);
      System.out.println(s);

            Elements element = document.select(
                    "#aspnetForm > div.udivright > div:nth-child(2) > table > tbody > tr > td:nth-child(1) > table:nth-child(2) > tbody > tr > td:nth-child(2) > div:nth-child(1) > font");

      System.out.println("个人积分为:"+element.text());
        }
    }
}
