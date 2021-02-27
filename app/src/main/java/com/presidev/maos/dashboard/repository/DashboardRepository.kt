package com.presidev.maos.dashboard.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.profile.mitra.model.Mitra
import java.util.ArrayList

class DashboardRepository {

    companion object{
        private const val TAG = "DashboardRepository"
    }

    private var resultMitra: MutableLiveData<List<Mitra>> = MutableLiveData()
    fun getResultsMitra(): LiveData<List<Mitra>> = resultMitra

    private var resultBook: MutableLiveData<List<Book>> = MutableLiveData()
    fun getResultsBook(): LiveData<List<Book>> = resultBook

    fun getMitra() {
        val produkData: MutableList<Mitra> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Mitra>()
        db.collection("mitra")
//                .orderBy("namaProyek")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val pp = document.toObject(Mitra::class.java)
                        pp.id = document.id
                        savedProdukList.add(pp)
                        produkData.add(pp)
                        Log.d(TAG, "getData size : ${savedProdukList.size} getData: $pp")
                    }
                    resultMitra.value = produkData
                    Log.d(TAG, "readProduk size final getData : ${savedProdukList.size}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
    }

    fun getBook() {
        val produkData: MutableList<Book> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Book>()
        db.collection("book")
                .orderBy("dateCreated" , Query.Direction.DESCENDING )
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val pp = document.toObject(Book::class.java)
                        pp.bookId = document.id
                        savedProdukList.add(pp)
                        produkData.add(pp)
                        Log.d(TAG, "getData size : ${savedProdukList.size} getData: $pp")
                    }
                    resultBook.value = produkData
                    Log.d(TAG, "readProduk size final getData : ${savedProdukList.size}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
    }


}