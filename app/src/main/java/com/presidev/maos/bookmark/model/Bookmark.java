package com.presidev.maos.bookmark.model;

import java.util.List;

public class Bookmark {

    private List<String> listBookId;

    public Bookmark() {}

    public Bookmark(List<String> listBookId) {
        this.listBookId = listBookId;
    }

    public List<String> getListBookId() {
        return listBookId;
    }

    public void setListBookId(List<String> listBookId) {
        this.listBookId = listBookId;
    }
}
