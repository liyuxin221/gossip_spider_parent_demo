package com.itheima.solr;

import com.itheima.pojo.News;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author: Liyuxin wechat:13011800146. @Title: IndexSearcherTest @ProjectName gossip_spider_parent
 * @date: 2019/1/17 23:57
 * @description: 索引库的查询操作
 */
public class IndexSearcherTest {

    /**
     * 查询索引库的入门
     */
    @Test
    public void indexSearchAll() throws SolrServerException {
        // 1.创建连接对象
        String baseUrl = "http://localhost:8080/solr/collection1";
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        // 2.查询条件(查询所有数据)
        SolrQuery solrQuery = new SolrQuery("*:*");

        // 3.提交查询
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);

        // 4.获取数据
        //    List<Object> beans = queryResponse.getBeans(Object.class);
        //    for (Object bean : beans) {
        //      // 提取查询结果
        //      System.out.println(bean.toString());
        //    }
        SolrDocumentList documents = queryResponse.getResults();
        for (SolrDocument document : documents) {
            // 遍历结果
            String id = (String) document.get("id");
            String content = (String) document.get("content");
            String title = (String) document.get("title");
            String editor = (String) document.get("editor");

            System.out.println(
                    "id:"
                            + id
                            + "\t"
                            + "title:"
                            + title
                            + "\t"
                            + "content:"
                            + content
                            + "\t"
                            + "editor:"
                            + editor
                            + "\t");
        }

        // 4.关闭资源
        httpSolrServer.shutdown();
    }

    /**
     * 查询索引库,返回JavaBean
     */
    @Test
    public void IndexSearcherBean() throws SolrServerException {
        // 1.创建连接对象
        String baseUrl = "http://localhost:8080/solr/collection1";
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        // 2.查询条件(查询所有数据)
        SolrQuery solrQuery = new SolrQuery("*:*");

        solrQuery.setRows(20);

        // 3.执行查询
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);

        // 4.获取结果
        List<News> beans = queryResponse.getBeans(News.class);
        for (News bean : beans) {
            // 遍历结果
            System.out.println(bean);
        }

        // 5.关闭资源
        httpSolrServer.shutdown();
    }

    /**
     * 复杂查询
     */
    @Test
    public void complexQueryTest() throws SolrServerException {
        // 1.创建连接对象
        String baseUrl = "http://localhost:8080/solr/collection1";
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        // 2.查询条件(查询所有数据)
        // 2.1 通配符查询
        //        SolrQuery solrQuery = new SolrQuery("title:*复盘");

        // 相识度查询--模糊查询
        //        SolrQuery solrQuery = new SolrQuery("title:小六童龄~2");

        //范围查询
        //SolrQuery solrQuery = new SolrQuery("id:[1 TO *]");

        // 布尔组合查询
        // AND MUST
        // OR SHOULD
        // NOT MUST_NOT
//        SolrQuery solrQuery = new SolrQuery("title:六小龄童 OR content:老婆");

        //子查询
        SolrQuery solrQuery = new SolrQuery("editor:水星 AND (content:小品 AND title:春晚 )");

        // 设置每页记录数
        solrQuery.setRows(20);

        // 3.执行查询
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);
        List<News> beans = queryResponse.getBeans(News.class);
        for (News bean : beans) {
            // 遍历查询结果
            System.out.println(bean);
        }

        // 4.关闭资源
        httpSolrServer.shutdown();
    }

    /**
     * 高级查询： 高亮  分页   排序
     */
    @Test
    public void sortPageTest() throws SolrServerException {
        // page pageSize
        Integer page = 2;
        Integer pageSize = 8;

        // 1.创建连接对象
        String baseUrl = "http://localhost:8080/solr/collection1";
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        // 2.创建查询条件
        SolrQuery solrQuery = new SolrQuery("title:*");
        // 2.1 分页
        Integer start = (page - 1) * pageSize;
        solrQuery.setStart(start);

        // 排序 id desc asc 字符串:拼音先后
        solrQuery.setSort("editor", SolrQuery.ORDER.desc);

        // 每页记录数
        solrQuery.setRows(pageSize);

        // 3.执行查询
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);

        // 4.获取结果
        List<News> beans = queryResponse.getBeans(News.class);

        for (News bean : beans) {
            // 遍历结果
            System.out.println(bean);
        }

        // 5.关闭资源
        httpSolrServer.shutdown();
    }

    /**
     * 高亮查询 查询出来的JavaBean没有高亮信息,需要自己替换
     */
    @Test
    public void highLightingTest() throws SolrServerException {
        // 1.创建连接对象
        String baseUrl = "http://localhost:8080/solr/collection1";
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        // 2.创建查询对象
        SolrQuery solrQuery = new SolrQuery("title:发布");

        // 3.设置高亮信息 开启高亮 设置高亮字段 高亮的前缀和后缀
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("title");
        solrQuery.addHighlightField("content");

        solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
        solrQuery.setHighlightSimplePost("</em>");

        // 4.执行高亮查询
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);

        // 重点:获取高亮的内容
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        //        for (String s : highlighting.keySet()) {
        //            // 遍历查询结果
        //            Map<String, List<String>> stringListMap = highlighting.get(s);
        //            for (String s1 : stringListMap.keySet()) {
        //                List<String> strings = stringListMap.get(s1);
        //                System.out.println(strings.toString());
        //            }
        //        }

        //通过查询普通结果,根据id,查到高亮结果
        List<News> beans = queryResponse.getBeans(News.class);

        for (News bean : beans) {
            // 遍历查询结果
            String id = bean.getId();
            Map<String, List<String>> fieldMap = highlighting.get(id);
            List<String> title = fieldMap.get("title");
            if (title != null || title.size() > 0) {
                bean.setTitle(title.get(0));
            }

            List<String> content = fieldMap.get("content");
            if (content != null || content.size() > 0) {
                bean.setContent(content.get(0));
            }

            System.out.println(bean);
        }

        //关闭资源
        httpSolrServer.shutdown();
    }
}
