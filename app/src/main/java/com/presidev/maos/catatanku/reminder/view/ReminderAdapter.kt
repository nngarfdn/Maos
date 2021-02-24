package com.presidev.maos.catatanku.reminder.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.presidev.maos.R
import com.presidev.maos.catatanku.reminder.model.Reminder
import java.text.SimpleDateFormat
import java.util.*

class ReminderAdapter(private val list: List<Reminder>) : RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_target, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val bookTitle: TextView = holder.itemView.findViewById(R.id.tv_book_title)
        val penyedia: TextView = holder.itemView.findViewById(R.id.tv_daily_pages)
        val status: TextView = holder.itemView.findViewById(R.id.tv_progress)
        var dayLeft = 0

        val calCurr: Calendar = Calendar.getInstance()
        val day: Calendar = Calendar.getInstance()

        day.setTime(SimpleDateFormat("yyyy/MM/dd").parse(list[position].returnDate))
        if (day.after(calCurr)) {
            dayLeft = (day.get(Calendar.DAY_OF_YEAR) - calCurr.get(Calendar.DAY_OF_YEAR))
        }

        bookTitle.setText(list[position].bookTitle)
        penyedia.setText(list[position].tempatPeminjam)


        val isReturned = list[position].isKembali

        if (isReturned.equals("true")) {
            status.setText("Sudah Kembali")
            status.setTextColor(holder.itemView.context.getResources().getColor(R.color.green))
        } else if ((isReturned.equals("false"))) {
            if (dayLeft <= 0) {
                status.setText("Kembalikan Sekarang")
                status.setTextColor(holder.itemView.context.getResources().getColor(R.color.red))
            } else if (dayLeft >= 0) {
                status.setText("$dayLeft Hari lagi")
                status.setTextColor(holder.itemView.context.getResources().getColor(R.color.orange))
            }
    }


    holder.itemView.setOnClickListener{
        val c = holder.itemView.context
        val intent = Intent(c, AddEditReminderActivity::class.java)
        intent.putExtra(AddEditReminderActivity.EXTRA_REMINDER, list[position])
        c.startActivity(intent)
    }
}
}