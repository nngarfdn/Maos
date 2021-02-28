package com.presidev.maos.bookmark.view

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.presidev.maos.R
import com.presidev.maos.bookdetail.BookDetailActivity
import com.presidev.maos.bookmark.view.BookmarkAdapter.FavoriteViewHolder
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.utils.AppUtils.loadImageFromUrl
import java.util.*

class BookmarkAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteViewHolder>(), Filterable {
    private val listItem = ArrayList<Book>()
    private val listItemFiltered = ArrayList<Book>()
    var data: ArrayList<Book>?
        get() = listItem
        set(listItem) {
            this.listItem.clear()
            this.listItem.addAll(listItem!!)
            listItemFiltered.clear()
            listItemFiltered.addAll(listItem)
            notifyDataSetChanged()
        }

    var countryFilterList = ArrayList<Book>()
    init {
        countryFilterList = data!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mitra_book_catalog, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = countryFilterList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            val intent = Intent(activity, BookDetailActivity::class.java)
            intent.putExtra(BookDetailActivity.EXTRA_BOOK, item)
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return countryFilterList.size
    }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imgPhoto: ImageView = itemView.findViewById(R.id.img_book_catalog)
        private var tvName: TextView = itemView.findViewById(R.id.txt_book_title)
        fun bind(item: Book) {
            tvName.text = item.title
            loadImageFromUrl(imgPhoto, item.photo)
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                countryFilterList = if (charSearch.isEmpty()) {
                    data!!
                } else {
                    val resultList = ArrayList<Book>()
                    for (row in data!!) {
                        if (row.title!!.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = countryFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as ArrayList<Book>
                notifyDataSetChanged()
            }

        }
    }
}