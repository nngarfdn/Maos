package com.presidev.maos.utils;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

public class ShimmerHelper {
    private final ShimmerFrameLayout shimmer;
    private final RecyclerView recyclerView;

    public ShimmerHelper(ShimmerFrameLayout shimmer, RecyclerView recyclerView){
        this.shimmer = shimmer;
        this.recyclerView = recyclerView;
    }

    public void show(){
        shimmer.startShimmer();
        shimmer.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    public void hide(){
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
