package com.presidev.maos.mitra.view.viewmodel

import androidx.lifecycle.ViewModel
import com.presidev.maos.mitra.model.Book
import com.presidev.maos.mitra.repository.BookRepository

class BookViewModel : ViewModel() {

    private val repository = BookRepository()

    fun insert(book: Book?) { repository.insert(book!!) }

}