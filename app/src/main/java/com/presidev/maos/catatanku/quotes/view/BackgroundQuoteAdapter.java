package com.presidev.maos.catatanku.quotes.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.presidev.maos.databinding.ItemQuoteBackgroundBinding;

import java.util.ArrayList;

import static com.presidev.maos.utils.AppUtils.loadImageFromUrl;

public class BackgroundQuoteAdapter extends RecyclerView.Adapter<BackgroundQuoteAdapter.ViewHolder> {
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

    @NonNull
    @Override
    public BackgroundQuoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuoteBackgroundBinding binding = ItemQuoteBackgroundBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BackgroundQuoteAdapter.ViewHolder holder, int position) {
        String backgroundQuote = backgroundQuoteList.get(position);
        holder.bind(backgroundQuote);
    }

    @Override
    public int getItemCount() {
        return backgroundQuoteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuoteBackgroundBinding binding;

        public ViewHolder(@NonNull ItemQuoteBackgroundBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String backgroundQuote) {
            loadImageFromUrl(binding.imgBackground, backgroundQuote);

            itemView.setOnClickListener(view -> {
                callback.onSelected(backgroundQuote);
            });
        }
    }

    interface OnQuoteBackgroundSelectCallback{
        void onSelected(String bgUrl);
    }
}
