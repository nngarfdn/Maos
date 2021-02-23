package com.presidev.maos.catatanku.reminder.view

import androidx.lifecycle.ViewModel
import com.presidev.maos.catatanku.reminder.model.Reminder
import com.presidev.maos.catatanku.reminder.repository.ReminderRepository
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.mitramanagement.repository.BookRepository

class ReminderViewModel : ViewModel() {
    private val repository = ReminderRepository()

    fun insert(reminder: Reminder) { repository.insert(reminder) }
    fun update(reminder: Reminder){ repository.update(reminder) }
    fun delete(id: String){ repository.delete(id)}
    fun getResultReminder()  = repository.getResultsReminder()
    fun loadResultReminder() = repository.getReminder()

}