package com.presidev.maos.catatanku.reminder.view

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.presidev.maos.R
import com.presidev.maos.auth.preference.AuthPreference
import com.presidev.maos.catatanku.helper.ReminderHelper.cancelReminder
import com.presidev.maos.catatanku.helper.ReturnReminder
import com.presidev.maos.catatanku.reminder.model.Reminder
import com.presidev.maos.utils.DateUtils.DATE_FORMAT
import kotlinx.android.synthetic.main.activity_add_edit_reminder.*
import java.text.SimpleDateFormat
import java.util.*

class AddEditReminderActivity : AppCompatActivity() {

    private lateinit var reminderViewModel: ReminderViewModel
    private var toDatePickerDialog: DatePickerDialog? = null
    private var dateFormatter: SimpleDateFormat? = null

    private var pref: AuthPreference? = null


    companion object {
        const val EXTRA_REMINDER = "extra_reminder"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_reminder)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pref = AuthPreference(this)
        reminderViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ReminderViewModel::class.java)


        dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.US)
        edt_return_date.inputType = InputType.TYPE_NULL
        edt_return_date.requestFocus()
        setDateTimeField()

        val intent = intent

        if (intent.hasExtra(EXTRA_REMINDER)) {

            btn_delete_reminder.visibility = View.VISIBLE
            val extra = intent.extras
            val reminder = extra?.getParcelable<Reminder>(EXTRA_REMINDER)

            edt_book_title.setText(reminder?.bookTitle)
            edt_borrow_date.setText(reminder?.tempatPeminjam)
            edt_return_date.setText(reminder?.returnDate)

            checkboxReturn.isChecked = reminder?.isKembali.equals("true")

            btn_save_reminder.setOnClickListener {
                val title = edt_book_title.text.toString()
                val borrowDate = edt_borrow_date.text.toString()
                val returnDate = edt_return_date.text.toString()
                val isReturned = checkboxReturn.isChecked.toString()
                if (title.isEmpty() || borrowDate.isEmpty() || returnDate.isEmpty()) {
                    Toast.makeText(this, "Lengkapi semua data", Toast.LENGTH_SHORT).show()
                } else {
                    val reminder =
                        Reminder(reminder?.id, pref?.id, title, borrowDate, returnDate, isReturned)
                    reminderViewModel.update(reminder)

                    if (reminder.isKembali.equals("false")) {
                        val returnReminder = ReturnReminder()
                        returnReminder.setReminder(this, reminder)
                    } else cancelReminder(this, reminder.id.hashCode())

                    Toast.makeText(this, "Berhasil Update", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            btn_delete_reminder.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Hapus pengingat")
                    .setMessage("Apakah kamu yakin ingin menghapusnya?")
                    .setNegativeButton("Batal", null)
                    .setPositiveButton("Ya") { _: DialogInterface?, i: Int ->
                        reminder?.let { it1 ->
                            reminderViewModel.delete(it1.id)
                            cancelReminder(this, it1.id.hashCode())
                        }
                        finish()
                    }.create().show()
            }
        } else {

            btn_delete_reminder.visibility = View.INVISIBLE
            btn_save_reminder.setOnClickListener {
                val title = edt_book_title.text.toString()
                val borrowDate = edt_borrow_date.text.toString()
                val returnDate = edt_return_date.text.toString()
                val isReturned = checkboxReturn.isChecked.toString()
                if (title.isEmpty() || borrowDate.isEmpty() || returnDate.isEmpty()) {
                    Toast.makeText(this, "Lengkapi semua data", Toast.LENGTH_SHORT).show()
                } else {
                    val reminder = Reminder("", pref?.id, title, borrowDate, returnDate, isReturned)
                    reminderViewModel.insert(reminder)

                    if (reminder.isKembali.equals("false")) {
                        val returnReminder = ReturnReminder()
                        returnReminder.setReminder(this, reminder)
                    } else cancelReminder(this, reminder.id.hashCode())

                    Toast.makeText(this, "Berhasil Menambahkan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun setDateTimeField() {
        val newCalendar: Calendar = Calendar.getInstance()

        toDatePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val newDate: Calendar = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)
                edt_return_date.setText(dateFormatter!!.format(newDate.time))
            },
            newCalendar.get(Calendar.YEAR),
            newCalendar.get(Calendar.MONTH),
            newCalendar.get(Calendar.DAY_OF_MONTH)
        )

        edt_return_date.setOnClickListener { toDatePickerDialog?.show() }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}