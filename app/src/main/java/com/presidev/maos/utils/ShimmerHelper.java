package com.presidev.maos.utils;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

public class ShimmerHelper {
    private final ShimmerFrameLayout shimmer;
    private final RecyclerView recyclerView;
    private ImageView imgEmpty;

    public ShimmerHelper(ShimmerFrameLayout shimmer, RecyclerView recyclerView){
        this.shimmer = shimmer;
        this.recyclerView = recyclerView;
    }

    public ShimmerHelper(ShimmerFrameLayout shimmer, RecyclerView recyclerView, ImageView imgEmpty){
        this.shimmer = shimmer;
        this.recyclerView = recyclerView;
        this.imgEmpty = imgEmpty;
    }

    public void show(){
        shimmer.startShimmer();
        shimmer.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        if (imgEmpty != null) imgEmpty.setVisibility(View.GONE);
    }

    public void hide(){
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
