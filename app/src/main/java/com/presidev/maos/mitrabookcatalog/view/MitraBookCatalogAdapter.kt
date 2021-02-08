package com.presidev.maos.mitrabookcatalog.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.presidev.maos.R
import com.presidev.maos.bookdetail.BookDetailActivity
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.mitramanagement.view.BookAdapter
import com.presidev.maos.mitramanagement.view.EditBookActivity
import com.squareup.picasso.Picasso

class MitraBookCatalogAdapter(private val list: List<Book>) : RecyclerView.Adapter<MitraBookCatalogAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_mitra_book_catalog, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val image : RoundedImageView = holder.itemView.findViewById(R.id.img_book_catalog)
        val title : TextView = holder.itemView.findViewById(R.id.txt_book_title)
        Picasso.get()
                .load(list[position].photo)
                .resize(120, 200) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(image)

        image.setCornerRadius(16F)
        title.setText(list[position].title)

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "${list[position].title}", Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, BookDetailActivity::class.java)
            intent.putExtra(BookDetailActivity.EXTRA_BOOK, list[position])
            holder.itemView.context.startActivity(intent)
        }
    }
}