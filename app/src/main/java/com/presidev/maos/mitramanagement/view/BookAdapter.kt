package com.presidev.maos.mitramanagement.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.presidev.maos.R
import com.presidev.maos.mitramanagement.model.Book
import com.squareup.picasso.Picasso

class BookAdapter (private val list: List<Book>) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val image : ImageView = holder.itemView.findViewById(R.id.imgBookAdapter)
        Picasso.get()
                .load(list[position].photo)
                .resize(150, 200) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(image)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditBookActivity::class.java)
            intent.putExtra(EditBookActivity.EXTRA_BOOK, list[position])
            holder.itemView.context.startActivity(intent)
        }
    }
}