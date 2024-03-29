package com.presidev.maos.catatanku.reminder.view

import androidx.lifecycle.ViewModel
import com.presidev.maos.catatanku.reminder.model.Reminder
import com.presidev.maos.catatanku.reminder.repository.ReminderRepository

class ReminderViewModel : ViewModel() {
    private val repository = ReminderRepository()

    fun insert(reminder: Reminder) {
        repository.insert(reminder)
    }

    fun update(reminder: Reminder) {
        repository.update(reminder)
    }

    fun delete(id: String) {
        repository.delete(id)
    }

    fun getResultReminder() = repository.getResultsReminder()
    fun loadResultReminder(uuid: String) = repository.getReminder(uuid)

}