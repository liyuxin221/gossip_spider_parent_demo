package com.itheima.spider.httpClient;

import jdk.nashorn.internal.runtime.regexp.joni.constants.EncloseType;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import sun.net.www.http.HttpClient;

/**
 * @author: Liyuxin wechat:13011800146.
 * @date: 2019/1/9
 * @description: ${todo}
 */
public class HttpClientGet {

    @Test
  public void test01() throws Exception{
    //      1.确定url
    String domin = "http://www.itcast.cn";
    //       2.发送请求,获取数据
    //       2.1创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

//        2.2设置请求方式:请求对象
        HttpGet httpGet = new HttpGet(domin);

//        2.3设置请求头信息
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36");

    //        2.4设置请求参数

    //        2.5发送请求,获取响应对象
        CloseableHttpResponse response = httpClient.execute(httpGet);

    //        3.获取响应
    //        3.1获取响应行

    //        3.2获取响应头
        Header[] allHeaders = response.getAllHeaders();
    for (Header allHeader : allHeaders) {
      System.out.println(allHeader);
      System.out.println("-------------");
    }

    System.out.println("===============");
    //        3.3获取响应码
    System.out.println("ProtocolVersion"+response.getProtocolVersion());
    System.out.println("Locale"+response.getLocale());

    //        3.4获取相应体(html)页面
        HttpEntity entity = response.getEntity();
        String s = EntityUtils.toString(entity, "utf-8");
    System.out.println(s);

//        从响应体里面获取

//        关闭资源
        response.close();
        httpClient.close();
  }
}
