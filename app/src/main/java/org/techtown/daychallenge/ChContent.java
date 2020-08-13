package org.techtown.daychallenge;

public class ChContent {
    int id;
    String category;
    String ch_content;
    int enable;

    public ChContent(int id, String category, String ch_content, int enable) {
        this.id = id;
        this.category = category;
        this.ch_content = ch_content;
        this.enable = enable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCh_content() {
        return ch_content;
    }

    public void setCh_content(String ch_content) {
        this.ch_content = ch_content;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }
}
