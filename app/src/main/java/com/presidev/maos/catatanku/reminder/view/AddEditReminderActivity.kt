package com.presidev.maos.catatanku.reminder.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.presidev.maos.R
import com.presidev.maos.catatanku.reminder.model.Reminder
import com.presidev.maos.location.viewmodel.LocationViewModel
import com.presidev.maos.utils.DateUtils.DATE_FORMAT
import kotlinx.android.synthetic.main.activity_add_edit_reminder.*
import java.text.SimpleDateFormat
import java.util.*

class AddEditReminderActivity : AppCompatActivity() {

    private lateinit var reminderViewModel: ReminderViewModel
    private var fromDatePickerDialog: DatePickerDialog? = null
    private var toDatePickerDialog: DatePickerDialog? = null
    private var dateFormatter: SimpleDateFormat? = null

    companion object {
        val EXTRA_REMINDER = "extra_reminder"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_reminder)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        reminderViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ReminderViewModel::class.java)

        dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.US)
        edt_return_date.inputType = InputType.TYPE_NULL
        edt_return_date.requestFocus()
        setDateTimeField()

        val intent = getIntent()

        if (intent.hasExtra(EXTRA_REMINDER)) {
            Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show()
        } else {
            btn_save_reminder.setOnClickListener {
                val title = edt_book_title.text.toString()
                val borrowDate = edt_borrow_date.text.toString()
                val returnDate = edt_return_date.text.toString()
                val isReturned = checkboxReturn.isChecked
                if (title.isEmpty() || borrowDate.isEmpty() || returnDate.isEmpty()) {
                    Toast.makeText(this, "Lengkapi semua data", Toast.LENGTH_SHORT).show()
                } else {
                    val reminder = Reminder("", title, borrowDate, returnDate, isReturned)
                    reminderViewModel.insert(reminder)
                    Toast.makeText(this, "Berhasil Menambahkan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun setDateTimeField() {
        val newCalendar: Calendar = Calendar.getInstance()

        toDatePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val newDate: Calendar = Calendar.getInstance()
            newDate.set(year, monthOfYear, dayOfMonth)
            edt_return_date.setText(dateFormatter!!.format(newDate.time))
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH))

        edt_return_date.setOnClickListener { toDatePickerDialog?.show() }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}