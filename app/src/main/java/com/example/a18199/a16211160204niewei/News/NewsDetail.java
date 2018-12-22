package com.example.a18199.a16211160204niewei.News;

import org.litepal.crud.LitePalSupport;

public class NewsDetail extends LitePalSupport {
    private String title;
    private String date;
    private String source;
    private String content;
    private String channid;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChannid() {
        return channid;
    }

    public void setChannid(String channid) {
        this.channid = channid;
    }
}
