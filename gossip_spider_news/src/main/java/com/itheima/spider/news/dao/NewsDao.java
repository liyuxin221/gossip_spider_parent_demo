package com.itheima.spider.news.dao;

import com.itheima.spider.news.pojo.News;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.beans.PropertyVetoException;

/**
 * @author: Liyuxin wechat:13011800146. @Title: NewDao @ProjectName gossip_spider_parent
 * @date: 2019/1/12 16:26
 * @description: 保存新闻的dao
 */
public class NewsDao extends JdbcTemplate {
    // 数据源
    private static ComboPooledDataSource dataSource;

    // 设置四大属性
    static {
        try {
            dataSource = new ComboPooledDataSource();
            dataSource.setUser("root");
            dataSource.setPassword("root");
            dataSource.setDriverClass("com.mysql.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/gossip?characterEncoding=utf-8");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构造方法
     */
    public NewsDao() {
        super(dataSource);
    }

    /**
     * 将news对象存入数据库
     *
     * @param news
     */
    public void saveNews(News news) {
        String sql =
                "insert into news(id,title,docurl,content,source,`time`,editor) values(?,?,?,?,?,?,?)";
        update(
                sql,
                news.getId(),
                news.getTitle(),
                news.getUrl(),
                news.getContent(),
                news.getSource(),
                news.getTime(),
                news.getEditor());
    }
}
