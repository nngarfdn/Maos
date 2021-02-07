package com.presidev.maos.dashboard.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.presidev.maos.R
import com.presidev.maos.mitrabookcatalog.view.MitraBookCatalogActivity
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.mitramanagement.view.BookAdapter
import com.presidev.maos.mitramanagement.view.EditBookActivity
import com.presidev.maos.profile.mitra.Mitra
import com.squareup.picasso.Picasso

class DashboardMitraAdapter(private val list: List<Mitra>) : RecyclerView.Adapter<DashboardMitraAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_mitra_dashboard, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val image : ImageView = holder.itemView.findViewById(R.id.img_item_book_dashboard)
        val namaMitra : TextView = holder.itemView.findViewById(R.id.txt_title_book)
        val alamatMitra : TextView = holder.itemView.findViewById(R.id.txt_address_book)
        Picasso.get()
                .load(list[position].logo)
                .resize(100, 100) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(image)

        namaMitra.setText(list[position].name)
        alamatMitra.setText(list[position].address)

        holder.itemView.setOnClickListener {
            val c = holder.itemView.context
            val intent = Intent(c, MitraBookCatalogActivity::class.java)
            intent.putExtra(MitraBookCatalogActivity.EXTRA_MITRA, list[position])
            c.startActivity(intent)
        }
    }
}