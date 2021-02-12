package com.presidev.maos.bookmark.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.presidev.maos.R;
import com.presidev.maos.mitramanagement.model.Book;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment implements BookmarkCallback {

    private BookmarkViewModel favoriteViewModel;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    public static BookmarkFragment newInstance(String param1, String param2) {
        BookmarkFragment fragment = new BookmarkFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    @Override
    public void onFinish(ArrayList<Book> listItem) {

    }
}

interface BookmarkCallback{
    void onFinish(ArrayList<Book> listItem);
}