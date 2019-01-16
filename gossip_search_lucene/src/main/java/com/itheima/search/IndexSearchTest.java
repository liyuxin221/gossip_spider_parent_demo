package com.itheima.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author: Liyuxin wechat:13011800146. @Title: IndexSearchTest @ProjectName gossip_spider_parent
 * @date: 2019/1/15 12:01
 * @description: 查询索引入门程序 根据查询条件查询结果
 */
public class IndexSearchTest {

    @Test
    public void indexSearchTest01() throws IOException {
        // 1.准备索引库(打开directory:FSDirectory)
        FSDirectory fsDirectory = FSDirectory.open(new File("gossip_search_lucene/IKlucene"));

        // 2.读取索引库:indexReader
        /** fsDirectory:索引库的目录 */
        DirectoryReader reader = DirectoryReader.open(fsDirectory);

        // 3.创建索引搜索器
        /** reader:索引库的读取对象 */
        IndexSearcher searcher = new IndexSearcher(reader);

        // 4.创建搜索条件
        /** Term对象就是一个词条(分词器分出来的一个词) fld:查询的字段名称 text:查询的关键字 */
        TermQuery query = new TermQuery(new Term("title", "施瓦辛格"));

        // 5.执行搜索,返回结果
        /**
         * query:查询对象:TermQuery n:求topN
         *
         * <p>topDocs:文档封装类对象
         */
        TopDocs topDocs = searcher.search(query, Integer.MAX_VALUE);

        // 6.遍历结果
        int totalHits = topDocs.totalHits;
        System.out.println(totalHits);

        // 命中的所有document(只有id,没有text)
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 获取文档的id
            int docid = scoreDoc.doc;
            System.out.println("文档的id:" + docid);

            // 根据文档的id,获取文档的内容
            Document doc = searcher.doc(docid);

            // 打印文档内容
            System.out.println("title" + doc.get("title"));
            System.out.println("url" + doc.get("url"));
            System.out.println("time" + doc.get("time"));
            System.out.println("content" + doc.get("content"));

            System.out.println("---------------------------");
        }
    }

    /**
     * 搜索入门程序
     */
    @Test
    public void test02() throws IOException {
        // 1.指定索引库
        FSDirectory fsDirectory = FSDirectory.open(new File("gossip_search_lucene/IKlucene"));

        // 2.索引库读取器
        IndexReader indexReader = DirectoryReader.open(fsDirectory);

        // 3.创建索引搜索器
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        // 4.创建搜索条件
        /** Term对象就是一个词条(分词器分出来的一个词)： 词条是不可在分割的, 词条可以是一个字, 也可以是一句话 fld：查询的字段名称 text: 查询的关键字 */
        // 创建词条
        Term term = new Term("title", "佘诗曼");
        TermQuery termQuery = new TermQuery(term);

        // 5.执行搜索
        // 搜索得到的是 含有索引的封装对象
        TopDocs topDocs = indexSearcher.search(termQuery, Integer.MAX_VALUE);

        // 6.通过索引找到原数据
        int totalHits = topDocs.totalHits;
        // 总条数
        System.out.println("查询数据总条数:" + totalHits);

        // 获取所有的 document
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("文档的id:" + scoreDoc);

            Document doc = indexSearcher.doc(scoreDoc.doc);

            String id = doc.get("id");
            String title = doc.get("title");
            String docurl = doc.get("docurl");
            String content = doc.get("content");
            String source = doc.get("source");
            String time = doc.get("time");
            String editor = doc.get("editor");

            System.out.println(
                    "id:" + id + "\r\n" + "title:" + title + "\r\n" + "docurl:" + docurl + "\r\n" + "content:"
                            + content + "\r\n" + "source:" + source + "\r\n" + "time:" + time + "\r\n" + "editor:"
                            + editor);
            System.out.println("----------------");
        }
    }
}
