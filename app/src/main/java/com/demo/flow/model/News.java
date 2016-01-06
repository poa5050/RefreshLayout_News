package com.demo.flow.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Administrator on 2016/1/4.
 */
public class News {
    @JSONField(name="title")
    private String title;

    @JSONField(name="abstract")
    private String abstractNews;

    @JSONField(name="url")
    private String url;

    @JSONField(name="datetime")
    private String datetime;

    @JSONField(name="img_url")
    private String img_url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractNews() {
        return abstractNews;
    }

    public void setAbstractNews(String abstractNews) {
        this.abstractNews = abstractNews;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    @Override
    public String toString() {
        return "title:" + this.title + " abstractNews:" + this.abstractNews + " datetime:"
                + this.datetime + " url:" + this.url + " img_url:" + this.img_url;
    }
}
