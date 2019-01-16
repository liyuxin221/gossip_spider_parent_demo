package com.itheima.pojo;

/**
 * @author: Liyuxin wechat:13011800146. @Title: News @ProjectName gossip_spider_parent
 * @date: 2019/1/15 20:36
 * @description:存储数据库数据的pojo
 */
public class News {
    // id
    private String id;
    // title
    private String title;
    // docurl
    private String docurl;
    // content
    private String content;
    // editor
    private String editor;
    // time
    private String time;
    // source
    private String source;

    @Override
    public String toString() {
        return "News{"
                + "id='"
                + id
                + '\''
                + ", title='"
                + title
                + '\''
                + ", docurl='"
                + docurl
                + '\''
                + ", content='"
                + content
                + '\''
                + ", editor='"
                + editor
                + '\''
                + ", time='"
                + time
                + '\''
                + ", source='"
                + source
                + '\''
                + '}';
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

    public String getDocurl() {
        return docurl;
    }

    public void setDocurl(String docurl) {
        this.docurl = docurl;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
