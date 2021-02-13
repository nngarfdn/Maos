package com.presidev.maos.bookmark.view;

import com.presidev.maos.mitramanagement.model.Book;

import java.util.ArrayList;

public interface BookmarkCallback {
        void onFinish(ArrayList<Book> listItem);
}
