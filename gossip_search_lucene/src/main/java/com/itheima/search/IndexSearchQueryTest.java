package com.itheima.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author: Liyuxin wechat:13011800146. @Title: IndexSearchQueryTest @ProjectName
 *     gossip_spider_parent
 * @date: 2019/1/15 15:00
 * @description:
 */
public class IndexSearchQueryTest {

    /**
     * 词条查询的演示 词条是最小的搜索单元,不可分割
     */
    @Test
    public void termQueryTest() throws IOException {
        Term term = new Term("title", "余诗曼");
        TermQuery termQuery = new TermQuery(term);

        // 执行查询
        executeQuery(termQuery);
    }

    /**
     * 通配符查询 WildCardQuery 0-n个字符:* 和数据库的% 一样 单个字符:? 数据库:_ :单个字符
     */
    @Test
    public void wildQueryTest() throws IOException {
        WildcardQuery title = new WildcardQuery(new Term("title", "*视频"));
        executeQuery(title);
    }

    /**
     * 模糊查询 通过替换,补位,移动 能够在二次切换内查询数据即可返回, 默认最大编辑次数:2次
     */
    @Test
    public void fuzzyQueryTest() throws IOException {
        FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term("title", "终前临"), 2);
        executeQuery(fuzzyQuery);
    }

    /**
     * numericRangeQuery 数值范围查询 只能对数值的field尽心查询,因索引库没有数值Field,无法演示
     */
    @Test
    public void numericRangeQueryTest() throws IOException {
        NumericRangeQuery<Integer> query =
                NumericRangeQuery.newIntRange(
                        "id",
                        Integer.getInteger("1084506439533199360"),
                        Integer.getInteger("1084506439600308224"),
                        true,
                        true);
        executeQuery(query);
    }

    /**
     * 布尔查询:多个查询条件的组合 与,或,非 Aquery && Bquery Aquery || Bquery !Aquery && Bquery
     */
    @Test
    public void booleanQueryTest() throws IOException {
        TermQuery termQuery = new TermQuery((new Term("title", "名字")));
        FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term("title", "你的"));

        BooleanQuery booleanClauses1 = new BooleanQuery();
        booleanClauses1.add(termQuery, BooleanClause.Occur.SHOULD);
        booleanClauses1.add(fuzzyQuery, BooleanClause.Occur.SHOULD);

        executeQuery(termQuery);
    }

    /**
     * 封装执行查询方法,控制台输出查询结果,不返回查询结果
     *
     * @param query 查询条件
     * @throws IOException
     */
    public void executeQuery(Query query) throws IOException {

        // 1.创建索引目录
        FSDirectory fsDirectory = FSDirectory.open(new File("gossip_search_lucene/IKlucene"));

        // 2.读取索引目录
        DirectoryReader reader = DirectoryReader.open(fsDirectory);

        // 3.创建索引搜索器
        IndexSearcher searcher = new IndexSearcher(reader);

        // 4.创建搜索对象

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
