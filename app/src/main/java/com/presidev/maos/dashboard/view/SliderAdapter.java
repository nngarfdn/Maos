package com.presidev.maos.dashboard.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.presidev.maos.R;
import com.presidev.maos.dashboard.model.Slider;

import java.util.ArrayList;
import java.util.List;
import com.smarteist.autoimageslider.SliderViewAdapter;

import static com.presidev.maos.utils.AppUtils.loadImageFromUrl;

public class SliderAdapter extends
        SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private List<Slider> mSliderItems = new ArrayList<>();

    public SliderAdapter(Context context) {
    }

    public void renewItems(List<Slider> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(Slider sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        Slider sliderItem = mSliderItems.get(position);

//        viewHolder.textViewDescription.setText(sliderItem.getDescription());
        viewHolder.textViewDescription.setTextSize(16F);
        viewHolder.textViewDescription.setTextColor(Color.WHITE);
        loadImageFromUrl(viewHolder.imageViewBackground, sliderItem.getImageUrl());

        viewHolder.itemView.setOnClickListener(v -> {
//                Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        final View itemView;
        final ImageView imageViewBackground;
        final ImageView imageGifContainer;
        final TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}