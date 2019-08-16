package com.soyikeji.work.work.entity;

/**
 * 未读信息
 * Created by Administrator on 2016/10/19.
 */
public class Notice {
    private String title;
    private String place;
    private String hour;
    private String prompt;
    private String content;


    public Notice() {
        super();
    }


    public Notice(String title, String place, String hour, String prompt,
                  String content) {
        super();
        this.title = title;
        this.place = place;
        this.hour = hour;
        this.prompt = prompt;
        this.content = content;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getPlace() {
        return place;
    }


    public void setPlace(String place) {
        this.place = place;
    }


    public String getHour() {
        return hour;
    }


    public void setHour(String hour) {
        this.hour = hour;
    }


    public String getPrompt() {
        return prompt;
    }


    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "Notice [title=" + title + ", place=" + place + ", hour=" + hour
                + ", prompt=" + prompt + ", content=" + content + "]";
    }



}
