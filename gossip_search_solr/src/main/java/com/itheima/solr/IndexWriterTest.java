package com.itheima.solr;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

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

        //5.关闭资源
        httpSolrServer.shutdown();
    }


}
