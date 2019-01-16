package com.itheima.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
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
 * @author: Liyuxin wechat:13011800146. @Title: IndexSearchQueryTest @ProjectName
 * gossip_spider_parent
 * @date: 2019/1/15 15:00
 * @description:
 */
public class IndexSearchQueryTest {

    @Test
    public void indexSearchQueryTest01() throws IOException {

        // 1.创建索引目录
        FSDirectory fsDirectory = FSDirectory.open(new File("gossip_search_lucene/IKlucene"));

        // 2.读取索引目录
        DirectoryReader reader = DirectoryReader.open(fsDirectory);

        // 3.创建索引搜索器
        IndexSearcher searcher = new IndexSearcher(reader);

        // 4.创建搜索对象
        TermQuery query = new TermQuery(new Term("source", "娱乐"));

        // 5.执行搜索
        TopDocs topDocs = searcher.search(query, Integer.MAX_VALUE);

        // 6.获取文档数据
        int len = topDocs.totalHits;
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for (ScoreDoc scoreDoc : scoreDocs) {
            //
            float score = scoreDoc.score;
            System.out.println("得分为:" + score);

            int doc = scoreDoc.doc;
            Document doc1 = searcher.doc(doc);

            String title = doc1.get("title");
            String url = doc1.get("url");
            String time = doc1.get("time");
            String content = doc1.get("content");
            String editor = doc1.get("editor");
            String source = doc1.get("source");

            System.out.println(
                    "查询的结果为:"
                            + "\t title:"
                            + title
                            + "\t url:"
                            + url
                            + "\t time:"
                            + time
                            + "\t content:"
                            + content
                            + "\t editor:"
                            + editor
                            + "\t source:"
                            + source);
        }
    }
}
