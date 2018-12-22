package com.example.a18199.a16211160204niewei.News;

import java.io.Serializable;
import java.util.Date;

public class NewsPreview implements Serializable {
    private String link;
    private boolean havePic;
    private String date;
    private String title;
    private String source;
    private String imageurls;
    public NewsPreview() {
    }

    @Override
    public String toString() {
        return "NewsPreview{" +
                "link='" + link + '\'' +
                ", havePic=" + havePic +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", imageurls='" + imageurls + '\'' +
                '}';
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isHavePic() {
        return havePic;
    }

    public void setHavePic(boolean havePic) {
        this.havePic = havePic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImageurls() {
        return imageurls;
    }

    public void setImageurls(String imageurls) {
        this.imageurls = imageurls;
    }
}
