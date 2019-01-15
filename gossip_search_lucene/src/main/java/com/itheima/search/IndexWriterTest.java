package com.itheima.search;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * @author: Liyuxin wechat:13011800146. @Title: IndexWriterTest @ProjectName gossip_spider_parent
 * @date: 2019/1/15 10:13
 * @description: 索引写入相关的测试(写入索引, 更新索引, 删除索引)
 */
public class IndexWriterTest {

    @Test
    public void indexWriterTest() throws IOException {
        // 1.创建索引写入的目录(磁盘,内存)Directory
        FSDirectory fsDirectory = FSDirectory.open(new File("gossip_search_lucene/IKlucene"));

        // 2.创建一个分词器对象(标准分词器) (创建索引和搜索索引要使用同一个分词器
        IKAnalyzer analyzer = new IKAnalyzer();

        // 3.创建一个索引写入的配置对象(IndexWriterConfig)
        /** Version matchVersion 版本号 Analyzer analyzer 分词器 */
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);

        // 4.创建索引写入器IndexWriter
        /** 第一个参数 索引写入的目录 第二个参数 索引配置对象 */
        IndexWriter writer = new IndexWriter(fsDirectory, config);

        // 1082123072372211712
        // 星爵向施瓦辛格女儿求婚成功 交往半年火速想结婚
        // http://ent.163.com/19/0114/16/E5GAPULO00038FO9.html
        // 网易娱乐1月14日报道
        // 北京时间1月14日消息，“星爵”克里斯·帕拉特和阿诺德·施瓦辛格的女儿凯瑟琳·施瓦辛格要结婚了。帕拉特透露已求婚，而凯瑟琳说了yes。“很高兴将跟你结婚，很骄傲能无畏并心怀信念与你一起生活”。 帕拉特与旧爱安娜·法瑞斯2017年8月宣布分手，12月申请离婚，结束8年婚姻。去年6月他与凯瑟琳恋情曝光，12月承认关系，如今订婚。
        // 责任编辑：张艺_NBJS7487
        // 2019-01-14 16:07:33
        // 网易娱乐
        Document document = new Document();
        document.add(new StringField("id", "1082123072372211712", Field.Store.YES));
        document.add(new TextField("title", "星爵向施瓦辛格女儿求婚成功 交往半年火速想结婚", Field.Store.YES));
        document.add(
                new StringField(
                        "url", "http://ent.163.com/19/0114/16/E5GAPULO00038FO9.html", Field.Store.YES));
        document.add(
                new StoredField(
                        "content",
                        "网易娱乐1月14日报道 北京时间1月14日消息，“星爵”克里斯·帕拉特和阿诺德·施瓦辛格的女儿凯瑟琳·施瓦辛格要结婚了。帕拉特透露已求婚，而凯瑟琳说了yes。“很高兴将跟你结婚，很骄傲能无畏并心怀信念与你一起生活”。 帕拉特与旧爱安娜·法瑞斯2017年8月宣布分手，12月申请离婚，结束8年婚姻。去年6月他与凯瑟琳恋情曝光，12月承认关系，如今订婚。"));
        document.add(new TextField("editor", "责任编辑：张艺_NBJS7487", Field.Store.YES));
        document.add(new StringField("time", "2019-01-14 16:07:33", Field.Store.YES));
        document.add(new TextField("source", "网易娱乐", Field.Store.YES));

        writer.addDocument(document, analyzer);

        writer.commit();
        writer.close();
    }
}
