package com.itheima.Pojo;

import org.apache.solr.client.solrj.beans.Field;

/**
 * @author: Liyuxin wechat:13011800146. @Title: News @ProjectName gossip_spider_parent
 * @date: 2019/1/15 20:36
 * @description:存储数据库数据的pojo
 */
public class News {
    // id
    @Field
    private String id;
    // title
    @Field
    private String title;
    // content
    @Field
    private String content;
    // editor
    @Field
    private String editor;

    @Override
    public String toString() {
        return "News{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", editor='" + editor + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }
}
