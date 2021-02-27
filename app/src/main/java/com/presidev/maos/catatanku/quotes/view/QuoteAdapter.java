package com.presidev.maos.catatanku.quotes.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.presidev.maos.catatanku.quotes.model.Quote;
import com.presidev.maos.databinding.ItemQuoteBinding;

import java.util.ArrayList;

import static com.presidev.maos.utils.AppUtils.loadImageFromUrl;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.ViewHolder> {
    private final ArrayList<Quote> quoteList = new ArrayList<>();

    public void setData(ArrayList<Quote> quoteList){
        this.quoteList.clear();
        this.quoteList.addAll(quoteList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuoteBinding binding = ItemQuoteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quote quote = quoteList.get(position);
        holder.bind(quote);
    }

    @Override
    public int getItemCount() {
        return quoteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuoteBinding binding;

        public ViewHolder(@NonNull ItemQuoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Quote quote) {
            loadImageFromUrl(binding.imgQuote, quote.getUrl());
        }
    }
}
