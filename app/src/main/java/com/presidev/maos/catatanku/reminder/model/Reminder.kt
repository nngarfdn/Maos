package com.presidev.maos.catatanku.reminder.model

data class Reminder(
        var id : String? = "",
        var bookTitle : String? = "",
        var tempatPeminjam : String? = "",
        var returnDate : String? = "",
        var isReturned : Boolean? = false
)