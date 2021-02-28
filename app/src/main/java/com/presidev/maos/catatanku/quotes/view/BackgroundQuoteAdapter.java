package com.presidev.maos.catatanku.quotes.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.presidev.maos.databinding.ItemQuoteBackgroundAddBinding;
import com.presidev.maos.databinding.ItemQuoteBackgroundBinding;

import java.util.ArrayList;

import static com.presidev.maos.utils.AppUtils.loadImageFromUrl;

public class BackgroundQuoteAdapter extends RecyclerView.Adapter<BackgroundQuoteAdapter.ViewHolder> {
    private static final int VIEW_TYPE_FOOTER = 0;
    private static final int VIEW_TYPE_CELL = 1;

    private final ArrayList<String> backgroundQuoteList = new ArrayList<>();
    private final OnQuoteBackgroundSelectCallback callback;

    public BackgroundQuoteAdapter(OnQuoteBackgroundSelectCallback callback){
        this.callback = callback;
    }

    public void setData(ArrayList<String> backgroundQuoteList){
        this.backgroundQuoteList.clear();
        this.backgroundQuoteList.addAll(backgroundQuoteList);
        notifyDataSetChanged();
    }

    public void addData(String backgroundQuote){
        this.backgroundQuoteList.add(backgroundQuote);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position){
        return (position == backgroundQuoteList.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_CELL;
    }

    @NonNull
    @Override
    public BackgroundQuoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CELL) {
            ItemQuoteBackgroundBinding binding = ItemQuoteBackgroundBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        } else if (viewType == VIEW_TYPE_FOOTER) {
            ItemQuoteBackgroundAddBinding binding = ItemQuoteBackgroundAddBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        } else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BackgroundQuoteAdapter.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_CELL){
            String backgroundQuote = backgroundQuoteList.get(position);
            holder.bind(backgroundQuote);
        } else if (getItemViewType(position) == VIEW_TYPE_FOOTER){
            holder.itemView.setOnClickListener(view -> callback.onPickFromGallery());
        }
    }

    @Override
    public int getItemCount() {
        return backgroundQuoteList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemQuoteBackgroundBinding binding;

        public ViewHolder(@NonNull ItemQuoteBackgroundBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String backgroundQuote) {
            loadImageFromUrl(binding.imgBackground, backgroundQuote);
            itemView.setOnClickListener(view -> callback.onSelected(backgroundQuote));
        }

        public ViewHolder(@NonNull ItemQuoteBackgroundAddBinding binding) {
            super(binding.getRoot());
        }
    }

    interface OnQuoteBackgroundSelectCallback{
        void onSelected(String bgUrl);
        void onPickFromGallery();
    }
}
