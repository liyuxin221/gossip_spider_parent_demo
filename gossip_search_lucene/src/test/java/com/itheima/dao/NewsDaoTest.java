package com.itheima.dao;

import com.itheima.pojo.News;
import org.junit.Test;

import java.util.List;

/**
 * @author: Liyuxin wechat:13011800146. @Title: NewsDaoTest @ProjectName gossip_spider_parent
 * @date: 2019/1/16 11:25
 * @description:
 */
public class NewsDaoTest {

    @Test
    public void queryList1() {
        NewsDao newsDao = new NewsDao();
        List<News> list = newsDao.queryList(1, 10);
        for (News news : list) {
            System.out.println(news);
        }
    }
}
