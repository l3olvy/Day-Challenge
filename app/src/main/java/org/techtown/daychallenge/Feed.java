package org.techtown.daychallenge;

public class Feed {
    int id;
    String picture;
    String content;

    //B Feed 아이템과 아이템 클릭시 필요한 정보들 & 함수들
    public Feed(int id, String content, String picture) {
        this.id = id;
        this.picture = picture;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
