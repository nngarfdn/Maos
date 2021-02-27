package com.presidev.maos.mitrabookcatalog.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.presidev.maos.mitramanagement.model.Book
import java.util.*

class MitraBookRepository {

    companion object{
        private const val TAG = "MitraBookRepository"
    }

    private var resultBookByMitraId: MutableLiveData<List<Book>> = MutableLiveData()
    fun getResultsByMitraId(): LiveData<List<Book>> = resultBookByMitraId

    private var resultBookByMitraIdPopuler: MutableLiveData<List<Book>> = MutableLiveData()
    fun getResultsByMitraIdPopuler(): LiveData<List<Book>> = resultBookByMitraIdPopuler


    fun getProyekByMitraID(id: String) {
        val produkData: MutableList<Book> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Book>()
        db.collection("book")
                .orderBy("dateCreated" , Query.Direction.DESCENDING )
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val kategoriDocument = document.data["mitraId"] as String
                        if (kategoriDocument == id) {
                            val pp = document.toObject(Book::class.java)
                            pp.bookId = document.id
                            savedProdukList.add(pp)
                            produkData.add(pp)
                            Log.d(TAG, "getDataByUUID size : ${savedProdukList.size} getDataByUUID: $pp")
                        }
                    }
                    resultBookByMitraId.value = produkData
                    Log.d(TAG, "readProduk size final getDataByUUID : ${savedProdukList.size}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
    }

    fun getProyekByMitraIDPopuler(id: String) {
        val produkData: MutableList<Book> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Book>()
        db.collection("book")
                .orderBy("waCount" , Query.Direction.DESCENDING )
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val kategoriDocument = document.data["mitraId"] as String
                        if (kategoriDocument == id) {
                            val pp = document.toObject(Book::class.java)
                            pp.bookId = document.id
                            savedProdukList.add(pp)
                            produkData.add(pp)
                            Log.d(TAG, "getDataByUUID size : ${savedProdukList.size} getDataByUUID: $pp")
                        }
                    }
                    resultBookByMitraIdPopuler.value = produkData
                    Log.d(TAG, "readProduk size final getDataByUUID : ${savedProdukList.size}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
    }
}