package com.itheima.pojo;

import org.apache.solr.client.solrj.beans.Field;

/**
 * @author: Liyuxin wechat:13011800146. @Title: News @ProjectName gossip_spider_parent
 * @date: 2019/1/18 18:25
 * @description: javaBean封装类
 */
public class News {

    // 新闻id
    @Field
    private String id;

    // 新闻标题
    @Field
    private String title;

    // 新闻内容
    @Field
    private String content;

    // 新闻的url
    @Field("docurl")
    private String url;

    // 新闻的编辑
    @Field
    private String editor;

    // 新闻的时间
    @Field
    private String time;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "News{"
                + "id='"
                + id
                + '\''
                + ", title='"
                + title
                + '\''
                + ", content='"
                + content
                + '\''
                + ", url='"
                + url
                + '\''
                + ", editor='"
                + editor
                + '\''
                + ", time='"
                + time
                + '\''
                + '}';
    }

}
