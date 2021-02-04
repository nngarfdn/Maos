package com.presidev.maos.mitra.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.presidev.maos.mitra.model.Book
import java.util.HashMap

class BookRepository {

    companion object {
        private const val TAG = "BookRepository"
    }

    private val database = FirebaseFirestore.getInstance()

    fun insert(book: Book) {
        val ref = database.collection("book").document()
        book.bookId = ref.id
        ref.set(hashMapBook(book))
                .addOnCompleteListener { task: Task<Void?> ->
                    if (task.isSuccessful) Log.d(TAG, "Document was added") else
                        Log.w(TAG, "Error adding document", task.exception) }
    }


    private fun hashMapBook(book: Book): Map<String, Any?> {
        val document: MutableMap<String, Any?> = HashMap()
        document["bookId"] = book.bookId
        document["mitraId"] = book.mitraId
        document["title"] = book.title
        document["ketersediaan"] = book.ketersediaan
        document["photo"] = book.photo
        document["description"] = book.description
        document["sinopsis"] = book.sinopsis

        return document
    }
}

