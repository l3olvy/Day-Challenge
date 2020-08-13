package org.techtown.daychallenge.ui.Feed;

public class Feed {
    int id;
    String ch_content;
    String content;;
    String picture;

    //B Feed 아이템과 아이템 클릭시 필요한 정보들 & 함수들
    public Feed(int id, String ch_content, String content, String picture) {
        this.id = id;
        this.ch_content = ch_content;
        this.content = content;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCh_content() {
        return ch_content;
    }

    public void setCh_content(String ch_content) {
        this.ch_content = ch_content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
