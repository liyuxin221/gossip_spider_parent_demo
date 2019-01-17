package com.itheima.solr;

import com.itheima.Pojo.News;
import com.itheima.dao.NewsDao;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Liyuxin wechat:13011800146. @Title: IndexWriterTest @ProjectName gossip_spider_parent
 * @date: 2019/1/17 12:20
 * @description: 索引库的修改
 */
public class IndexWriterTest {

    @Test
    public void test01() throws Exception {
        // 1.solr 连接对象 solrServer
        String baseUrl = "http://localhost:8080/solr/collection1";
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        // 2.创建document entry
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", 5);
        doc.addField("title", "王祖蓝陪产部分画面曝光，为李亚男整理头发画面超有爱");
        doc.addField("content", "王祖蓝录制完节目马上赶回香港去陪产 路上回忆和老婆的点点滴滴");
        doc.addField("editor", "吃瓜群众");

        // 3.添加索引
        httpSolrServer.add(doc);

        // 4.提交
        httpSolrServer.commit();

        // 5.关闭资源
        httpSolrServer.shutdown();
    }

    /**
     * 索引库写入操作:一次添加多条
     */
    @Test
    public void indexWriterManyTest() throws IOException, SolrServerException {
        // 1.创建solr连接对象
        String baseUrl = "http://localhost:8080/solr/collection1";
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        // 2.创建文档对象
        ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();

        for (int i = 1; i <= 10; i++) {
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", i);
            doc.addField("title", "王祖蓝陪产部分画面曝光，为李亚男整理头发画面超有爱" + i);
            doc.addField("content", "王祖蓝录制完节目马上赶回香港去陪产 路上回忆和老婆的点点滴滴" + i);
            doc.addField("editor", "吃瓜群众" + i);

            docList.add(doc);
        }

        // 3.添加列表
        httpSolrServer.add(docList);

        // 4.提交
        httpSolrServer.commit();

        // 5.关闭
        httpSolrServer.shutdown();
    }

    /**
     * 索引库写入操作:用Dao写入多个数据
     */
    @Test
    public void indexWriterDao() throws IOException, SolrServerException {
        // 1.创建solr连接对象
        String baseUrl = "http://localhost:8080/solr/collection1";
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        // 2.获取多个news对象
        NewsDao newsDao = new NewsDao();
        List<News> newsList = newsDao.queryList(2, 20);

        // 3.写入JavaBean
        httpSolrServer.addBeans(newsList);

        // 4.提交
        httpSolrServer.commit();

        // 5.关闭资源
        httpSolrServer.shutdown();
    }

    /**
     * 写入JavaBean对象:写入索引库
     */
    @Test
    public void IndexWriterByBean() throws IOException, SolrServerException {
        // 1.创建连接对象
        String baseUrl = "http://localhost:8080/solr/collection1";
        HttpSolrServer server = new HttpSolrServer(baseUrl);

        // 2.创建JavaBean
        News news = new News();
        news.setId("21");
        news.setContent("第1期抢先看：美岐超越崩溃大哭，成员们精力透支集体想回家？");
        news.setEditor("腾讯娱乐");
        news.setTitle("烎2019潮音发布夜：王源唱哭，吴亦凡佛系感悟人生");

        // 3.写入JavaBean
        server.addBean(news);

        // 4.提交
        server.commit();

        // 5.关闭资源
        server.shutdown();
    }

    /**
     * 修改索引库的操作
     */
    @Test
    public void indexWriterUpdateTest() throws IOException, SolrServerException {
        // 1.创建连接对象
        String baseUrl = "http://localhost:8080/solr/collection1";
        HttpSolrServer server = new HttpSolrServer(baseUrl);

        // 2.创建document对象
        SolrInputDocument document = new SolrInputDocument();

        document.addField("id", 1);
        document.addField("content", "李易峰在线“调戏”王思聪，羡慕隔壁老王");
        // document.addField("editor","");
        document.addField("title", "吃瓜群众update");

        // 3.提交
        server.add(document);
        server.commit();

        // 4.关闭资源
        server.shutdown();
    }

    /**
     * 索引库的删除
     */
    @Test
    public void indexDeleteTest() throws IOException, SolrServerException {

        // 1.创建连接对象
        String baseUrl = "http://localhost:8080/solr/collection1";
        HttpSolrServer solrServer = new HttpSolrServer(baseUrl);

        // 2.根据id删除
        //    solrServer.deleteById("1");
        // 2.根据查询条件
        //    solrServer.deleteByQuery("title:王祖蓝");
        // 2.删除全部
        solrServer.deleteByQuery("*:*");

        // 3.提交
        solrServer.commit();

        // 4.关闭资源
        solrServer.shutdown();
    }
}
