package com.presidev.maos.catatanku.reminder.view

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.presidev.maos.R
import com.presidev.maos.catatanku.reminder.model.Reminder
import com.presidev.maos.utils.DateUtils.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

class ReminderAdapter(private val list: List<Reminder>) :
    RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_target, parent, false)
    )

    override fun getItemCount(): Int = list.size

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val bookTitle: TextView = holder.itemView.findViewById(R.id.tv_book_title)
        val penyedia: TextView = holder.itemView.findViewById(R.id.tv_daily_pages)
        val status: TextView = holder.itemView.findViewById(R.id.tv_progress)
        var dayLeft = 0

        val calCurr: Calendar = Calendar.getInstance()
        val day: Calendar = Calendar.getInstance()

        day.time = SimpleDateFormat(DATE_FORMAT).parse(list[position].returnDate)
        if (day.after(calCurr)) {
            dayLeft = (day.get(Calendar.DAY_OF_YEAR) - calCurr.get(Calendar.DAY_OF_YEAR))
        }

        bookTitle.text = list[position].bookTitle
        penyedia.text = list[position].tempatPeminjam


        val isReturned = list[position].isKembali

        if (isReturned.equals("true")) {
            status.text = "Sudah Kembali"
            status.setTextColor(holder.itemView.context.resources.getColor(R.color.green))
        } else if ((isReturned.equals("false"))) {
            if (dayLeft <= 0) {
                status.text = "Kembalikan Sekarang"
                status.setTextColor(holder.itemView.context.resources.getColor(R.color.red))
            } else if (dayLeft >= 0) {
                status.text = "$dayLeft Hari lagi"
                status.setTextColor(holder.itemView.context.resources.getColor(R.color.orange))
            }
        }


        holder.itemView.setOnClickListener {
            val c = holder.itemView.context
            val intent = Intent(c, AddEditReminderActivity::class.java)
            intent.putExtra(AddEditReminderActivity.EXTRA_REMINDER, list[position])
            c.startActivity(intent)
        }
    }
}