package com.presidev.maos.mitramanagement.view

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.presidev.maos.callback.OnImageUploadCallback
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.mitramanagement.repository.BookRepository

class BookViewModel : ViewModel() {

    private val repository = BookRepository()

    fun insert(book: Book) {
        repository.insert(book)
    }

    fun update(book: Book) {
        repository.update(book)
    }

    fun delete(idBook: String) {
        repository.delete(idBook)
    }

    fun getResultByMitraId() = repository.getResultsByMitraId()
    fun loadResultBymitraId(mitraId: String) = repository.getProyekByMitraID(mitraId)

    fun uploadImage(
        context: Context?,
        portfolioId: String?,
        uri: Uri?,
        fileName: String?,
        callback: OnImageUploadCallback?
    ) {
        if (callback != null) {
            repository.uploadImage(context, portfolioId!!, uri, fileName!!, callback)
        }
    }

}