package com.itheima.solrCloud;

import com.itheima.constant.CloudConstant;
import com.itheima.pojo.News;
import com.itheima.pojo.News2;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author: Liyuxin wechat:13011800146. @Title: IndexWriterTest @ProjectName gossip_spider_parent
 * @date: 2019/1/18 17:36
 * @description:
 */
public class CloudIndexWriterTest {

    /**
     * 添加
     */
    @Test
    public void indexWriterTest() throws IOException, SolrServerException {
        // 1.创建solr集群的连接对象
        // zookeeper集群的地址
        String zkHost = "node01:2181,node02:2181,node03:2182";
        CloudSolrServer cloudSolrServer = new CloudSolrServer(zkHost);

        // 2.告诉连接对象连接哪个索引库
        cloudSolrServer.setDefaultCollection("collection1");

        // 从zookeeper设置连接solr的连接的超时时间
        cloudSolrServer.setZkConnectTimeout(5000);

        // 设置zookeeper客户端连接的超时时间
        cloudSolrServer.setZkClientTimeout(5000);

        // 3.获取连接
        cloudSolrServer.connect();

        // 4.创建文档
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "1");
        document.addField("title", "刘雯穿“雨衣装”亮相T台，冷艳短发造型气场炸裂");
        document.addField(
                "content",
                "近日，中国美女模特刘雯亮相2019秋冬米兰男装周，她现场穿着一件穿“雨衣”霸气走秀，美女短发酷感眼线上挑更显“高冷美”。中国美女模特刘雯亮相2019秋冬米兰男装周，她现场穿着一件穿“雨衣”霸气走秀，……");
        document.addField("docurl", "https://xw.qq.com/cmsid/20190113A0CSNY00");
        document.addField("editor", "腾讯新闻");
        document.addField("time", new Date());
        document.addField("source", "轻松视角");

        // 5.添加文档
        cloudSolrServer.add(document);

        // 6.提交
        cloudSolrServer.commit();

        // 7.关闭资源
        cloudSolrServer.shutdown();
    }

    /**
     * 修改(id相同即可)
     */
    @Test
    public void updateIndexTest() throws IOException, SolrServerException {
        // 1.创建集群连接对象
        String zkHost = CloudConstant.zkHost;
        CloudSolrServer cloudSolrServer = new CloudSolrServer(zkHost);

        // 2.设置默认连接的索引库
        cloudSolrServer.setDefaultCollection(CloudConstant.defalutCollection02);

        // 3.获取连接
        cloudSolrServer.connect();

        // 4.创建javaBean对象
        News news = new News();

        news.setId("3");
        news.setTitle("周杰伦萧敬腾演唱会上抢座位 周董靠这一点获胜woshixiugai");
        news.setContent(
                "周杰伦萧敬腾演唱会上抢座位 （来源：）  网易娱乐1月8日报道 在2017年9月的周杰伦演唱会上，作为表演嘉宾的萧敬腾跟正在表演弹唱的周杰伦抢起了座位，周杰伦也毫不相让，二人笑着挤来挤去，而不论萧敬腾怎么挤，周杰伦都在座位上安然不动。  被网友调侃道：“老萧啊你是抢不过周董的，谁让你没喝珍珠奶茶”、“果然周董奶茶不是白喝的”，“两位是小学生吗？为什么这么可爱”等等。");
        news.setEditor("韩冲");
        news.setUrl("http://ent.163.com/19/0108/13/E50I6RJC00038FO9.html");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String dateStr = sdf.format(date);

        news.setTime(dateStr);

        // 5.添加javaBean
        cloudSolrServer.addBean(news);

        // 6.提交
        cloudSolrServer.commit();

        // 7.关闭资源
        cloudSolrServer.shutdown();
    }

    /**
     * 删除
     */
    @Test
    public void deleteIndexTest() throws IOException, SolrServerException {
        // 1.创建solrCloud的链接对象
        CloudSolrServer cloudSolrServer = new CloudSolrServer(CloudConstant.zkHost);

        // 2.设置默认索引库
        cloudSolrServer.setDefaultCollection(CloudConstant.defalutCollection02);

        // 3.获取连接
        cloudSolrServer.connect();

        // 4.执行删除:根据id 根据查询条件 删除所有
        String query = "title:周杰伦";
        // 根据id删除
        cloudSolrServer.deleteByQuery(query);

        // 5.提交删除
        cloudSolrServer.commit();

        // 6.关闭
        cloudSolrServer.shutdown();
    }

    /**
     * 简单查询 getBeans
     */
    @Test
    public void queryIndexTest() throws SolrServerException {

        // 1.创建SolrCloud的链接对象
        CloudSolrServer cloudSolrServer = new CloudSolrServer(CloudConstant.zkHost);

        // 2.设置默认索引库
        cloudSolrServer.setDefaultCollection(CloudConstant.defalutCollection02);

        // 3.获取连接
        cloudSolrServer.connect();

        // 4.创建查询对象
        SolrQuery solrQuery = new SolrQuery("*:*");

        // 5.获取查询结果
        QueryResponse response = cloudSolrServer.query(solrQuery);
        List<News2> beans = response.getBeans(News2.class);
        for (News2 bean : beans) {
            // 遍历查询结果
            System.out.println(bean.toString());
        }

//        for (SolrDocument result : results) {
//            // 遍历查询结果
//            System.out.println(result);
//        }

        //6.关闭资源
        cloudSolrServer.shutdown();
    }

    /**
     * 简单查询 getResults
     */
    @Test
    public void queryIndexTest02() throws SolrServerException {

        // 1.创建SolrCloud的链接对象
        CloudSolrServer cloudSolrServer = new CloudSolrServer(CloudConstant.zkHost);

        // 2.设置默认索引库
        cloudSolrServer.setDefaultCollection(CloudConstant.defalutCollection02);

        // 3.获取连接
        cloudSolrServer.connect();

        // 4.创建查询对象
        SolrQuery solrQuery = new SolrQuery("title:周杰伦");

        // 5.获取查询结果
        QueryResponse response = cloudSolrServer.query(solrQuery);
        SolrDocumentList results = response.getResults();
        for (SolrDocument result : results) {
            // 遍历查询结果
            News news = new News();
            news.setId((String) result.get("id"));
            news.setTitle((String) result.get("title"));
            news.setContent((String) result.get("content"));
            news.setEditor((String) result.get("editor"));
            news.setUrl((String) result.get("docurl"));
            // "time": "2019-01-19T23:16:39Z"
            Date time = (Date) result.get("time");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            long localTime = time.getTime() - 1000 * 8 * 60 * 60;

            String localTimeStr = simpleDateFormat.format(localTime);
            news.setTime(localTimeStr);

            System.out.println(news);
        }

        //6.关闭资源
        cloudSolrServer.shutdown();
    }
}
