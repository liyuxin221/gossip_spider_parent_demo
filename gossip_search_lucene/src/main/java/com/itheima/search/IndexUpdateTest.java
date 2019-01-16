package com.itheima.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * @author: Liyuxin wechat:13011800146. @Title: IndexUpdateTest @ProjectName gossip_spider_parent
 * @date: 2019/1/16 15:42
 * @description: 更新, 删除索引的测试
 */
public class IndexUpdateTest {
    /**
     * 更新索引:先删除索引,再更新
     */
    @Test
    public void updateTest() throws IOException {
        // 1.打开索引库
        FSDirectory fsDirectory = FSDirectory.open(new File("gossip_search_lucene/IKlucene"));

        // 2.创建分词器
        IKAnalyzer ikAnalyzer = new IKAnalyzer();

        // 3.配置对象
        IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LATEST, ikAnalyzer);

        // 4.创建索引写入器
        IndexWriter indexWriter = new IndexWriter(fsDirectory, writerConfig);

        // 5.更新索引
        Document doc = new Document();

        TextField source = new TextField("editor", "腾讯娱乐-2", Field.Store.YES);

        doc.add(source);

        indexWriter.updateDocument(new Term("editor", "腾讯娱乐"), doc);

        indexWriter.commit();
        indexWriter.close();
        ikAnalyzer.close();
        fsDirectory.close();
    }

    /**
     * 删除索引
     */
    @Test
    public void delIndexTest() throws IOException, ParseException {
        // 1.打开索引库
        FSDirectory fsDirectory = FSDirectory.open(new File("gossip_search_lucene/IKlucene"));
        // 2.创建analyzer
        IKAnalyzer ikAnalyzer = new IKAnalyzer();
        // 3.创建indexWriterConfig
        IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LATEST, ikAnalyzer);
        // 4. 创建indexWriter
        IndexWriter indexWriter = new IndexWriter(fsDirectory, writerConfig);
        // 5.删除索引
        // 删除条件
        /**
         * termquery是按照一个词条匹配删除
         * queryParser，需要进行分词后，形成多字段匹配删除 */
        QueryParser queryParser = new QueryParser("editor", ikAnalyzer);
        Query query = queryParser.parse("贵圈八姨太");
        Term term = new Term("editor", "腾讯");

        indexWriter.deleteDocuments(query);

        indexWriter.commit();
        indexWriter.close();
        ikAnalyzer.close();
        fsDirectory.close();
    }
}
