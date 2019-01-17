package com.itheima.dao;

import com.itheima.Pojo.News;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.beans.PropertyVetoException;
import java.util.List;

/**
 * @author: Liyuxin wechat:13011800146. @Title: NewsDao @ProjectName gossip_spider_parent
 * @date: 2019/1/15 20:36
 * @description: 操作数据库的dao
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
     * 获取某一页的数据
     *
     * @param currentPage 当前页码
     * @param rows        每页行数
     * @return ArrayList<News>
     */
    public List<News> queryList(int currentPage, int rows) {

        int starIndex = (currentPage - 1) * rows;
        String sql = " select id,title,content,editor from news limit ? , ?";
        List<News> list;
        list = query(sql, new BeanPropertyRowMapper<News>(News.class), starIndex, rows);

        return list;
    }

}
