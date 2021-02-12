package com.presidev.maos.bookmark.view;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.presidev.maos.R;
import com.presidev.maos.bookdetail.BookDetailActivity;
import com.presidev.maos.mitramanagement.model.Book;

import java.util.ArrayList;
import java.util.List;

import static com.presidev.maos.utils.AppUtils.loadImageFromUrl;


public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.FavoriteViewHolder> {
    private final Activity activity;
    private final ArrayList<Book> listItem = new ArrayList<>();
    private ArrayList<Book> listItemFiltered = new ArrayList<>();

    public BookmarkAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setData(List<Book> listItem) {
        this.listItem.clear();
        this.listItem.addAll(listItem);

        this.listItemFiltered.clear();
        this.listItemFiltered.addAll(listItem);

        notifyDataSetChanged();
    }

    public ArrayList<Book> getData() {
        return listItem;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mitra_book_catalog, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Book item = listItemFiltered.get(position);
        holder.bind(item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, BookDetailActivity.class);
                intent.putExtra(BookDetailActivity.EXTRA_BOOK, item);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemFiltered.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
       ImageView imgPhoto;
       TextView tvName;
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txt_book_title);
            imgPhoto = itemView.findViewById(R.id.img_book_catalog);
        }

        public void bind(Book item) {
            tvName.setText(item.getTitle());
            loadImageFromUrl(imgPhoto, item.getPhoto());
        }

    }
}

