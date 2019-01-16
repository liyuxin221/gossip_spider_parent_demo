package com.itheima.search;

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
        //
        // 北京时间1月14日消息，“星爵”克里斯·帕拉特和阿诺德·施瓦辛格的女儿凯瑟琳·施瓦辛格要结婚了。帕拉特透露已求婚，而凯瑟琳说了yes。“很高兴将跟你结婚，很骄傲能无畏并心怀信念与你一起生活”。 帕拉特与旧爱安娜·法瑞斯2017年8月宣布分手，12月申请离婚，结束8年婚姻。去年6月他与凯瑟琳恋情曝光，12月承认关系，如今订婚。
        // 责任编辑：张艺_NBJS7487
        // 2019-01-14 16:07:33
        // 网易娱乐
        Document document = new Document();
        //        document.add(new StringField("id", "1082123072372211712", Field.Store.YES));
        //        document.add(new TextField("title", "星爵向施瓦辛格女儿求婚成功 交往半年火速想结婚", Field.Store.YES));
        //        document.add(
        //                new StringField(
        //                        "url", "http://ent.163.com/19/0114/16/E5GAPULO00038FO9.html",
        // Field.Store.YES));
        //        document.add(
        //                new StoredField(
        //                        "content",
        //                        "网易娱乐1月14日报道
        // 北京时间1月14日消息，“星爵”克里斯·帕拉特和阿诺德·施瓦辛格的女儿凯瑟琳·施瓦辛格要结婚了。帕拉特透露已求婚，而凯瑟琳说了yes。“很高兴将跟你结婚，很骄傲能无畏并心怀信念与你一起生活”。 帕拉特与旧爱安娜·法瑞斯2017年8月宣布分手，12月申请离婚，结束8年婚姻。去年6月他与凯瑟琳恋情曝光，12月承认关系，如今订婚。"));
        //        document.add(new TextField("editor", "责任编辑：张艺_NBJS7487", Field.Store.YES));
        //        document.add(new StringField("time", "2019-01-14 16:07:33", Field.Store.YES));
        //        document.add(new TextField("source", "网易娱乐", Field.Store.YES));

        document.add(new StringField("id", "1", Field.Store.YES));
        document.add(new TextField("title", "1", Field.Store.YES));
        document.add(new StringField("url", "1", Field.Store.YES));
        document.add(new StoredField("content", "1"));
        document.add(new TextField("editor", "责任编辑：张艺_NBJS7487", Field.Store.NO));
        document.add(new StringField("time", "2019-01-14 16:07:33", Field.Store.YES));
        document.add(new StoredField("source", "网易娱乐"));

        writer.addDocument(document, analyzer);
        writer.commit();

        document.add(new StringField("id", "2", Field.Store.YES));
        document.add(new TextField("title", "2", Field.Store.YES));
        document.add(new StringField("url", "2", Field.Store.YES));
        document.add(new StoredField("content", "1"));
        document.add(new TextField("editor", "2责任编辑：张艺_NBJS7487", Field.Store.NO));
        document.add(new StringField("time", "2 2019-01-14 16:07:33", Field.Store.YES));
        document.add(new StoredField("source", "2 网易娱乐"));

        writer.addDocument(document, analyzer);

        writer.commit();
        writer.close();
    }

    /**
     * 练习创建索引
     */
    @Test
    public void test02() throws IOException {
        // 1.创建目录
        FSDirectory fsDirectory = FSDirectory.open(new File("gossip_search_lucene/IKlucene"));

        //2.分词器对象
        IKAnalyzer ikAnalyzer = new IKAnalyzer();

        // 3.创建索引写入器配置对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, ikAnalyzer);

        //4.创建索引写入器,配置写入器配置对象
        IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);

        // 5.创建文档对象
        Document document = new Document();

        // 6.添加字段
        // 1084506439533199360
        // 佘诗曼被网红拉着拍视频，滤镜下惨变“蛇精”，吓得本尊都看呆了
        // https://xw.qq.com/cmsid/20190113V0P3E400
        // 佘诗曼被偶遇美成小仙女，但是在网红镜头下惨变“蛇精”
        // 嘻嘻盘娱
        // 2019-01-13 17:52:40
        // 嘻嘻盘娱
        StringField id = new StringField("id", "1084506439533199360", Field.Store.YES);
        TextField title = new TextField("title", "佘诗曼被网红拉着拍视频，滤镜下惨变“蛇精”，吓得本尊都看呆了", Field.Store.YES);
        StringField docurl = new StringField("docurl", "https://xw.qq.com/cmsid/20190113V0P3E400", Field.Store.YES);
        TextField content = new TextField("content", "佘诗曼被偶遇美成小仙女，但是在网红镜头下惨变“蛇精", Field.Store.YES);
        TextField source = new TextField("source", "嘻嘻盘娱", Field.Store.YES);
        StringField time = new StringField("time", "2019-01-13 17:52:40", Field.Store.YES);
        StringField editor = new StringField("editor", "嘻嘻盘娱", Field.Store.YES);

        document.add(id);
        document.add(title);
        document.add(docurl);
        document.add(content);
        document.add(source);
        document.add(time);
        document.add(editor);

        //7.将文档通过索引写入器写入目录类
        indexWriter.addDocument(document, ikAnalyzer);
        indexWriter.commit();

        //8.关闭资源
        indexWriter.close();
        ikAnalyzer.close();
        fsDirectory.close();
    }
}
