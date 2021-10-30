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
import com.presidev.maos.profile.mitra.model.Mitra
import com.presidev.maos.utils.AppUtils.*

class DashboardMitraAdapter(private val list: List<Mitra>) :
    RecyclerView.Adapter<DashboardMitraAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_mitra_dashboard, parent, false)
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val image: ImageView = holder.itemView.findViewById(R.id.img_item_book_dashboard)
        val namaMitra: TextView = holder.itemView.findViewById(R.id.txt_title_book)
        val alamatMitra: TextView = holder.itemView.findViewById(R.id.txt_address_book)
        loadImageFromUrl(image, list[position].logo)

        namaMitra.text = list[position].name
        alamatMitra.text = setFullAddress(
            null,
            list[position].province,
            getSimpleRegency(list[position].regency),
            list[position].district
        )

        holder.itemView.setOnClickListener {
            val c = holder.itemView.context
            val intent = Intent(c, MitraBookCatalogActivity::class.java)
            intent.putExtra(MitraBookCatalogActivity.EXTRA_MITRA, list[position])
            c.startActivity(intent)
        }
    }
}