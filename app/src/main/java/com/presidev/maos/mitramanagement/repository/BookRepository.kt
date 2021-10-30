package com.presidev.maos.mitramanagement.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.presidev.maos.callback.OnImageUploadCallback
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.utils.ImageUtils.convertUriToByteArray
import com.presidev.maos.utils.ImageUtils.getCompressedByteArray
import java.util.*

class BookRepository {

    private var resultBookByMitraId: MutableLiveData<List<Book>> = MutableLiveData()
    private val storage = FirebaseStorage.getInstance()

    fun getResultsByMitraId(): LiveData<List<Book>> = resultBookByMitraId

    companion object {
        private const val TAG = "BookRepository"
        const val FOLDER_BUKU = "buku"
    }

    private val database = FirebaseFirestore.getInstance()

    fun update(book: Book) {
        val idProduk = book.bookId
        val item = hashMapBook(book)

        if (idProduk != null) {
            database.collection("book").document(idProduk)
                .set(item)
                .addOnSuccessListener {
                    Log.d(TAG, "Succes update")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error adding document", e)
                }
        }
    }


    fun delete(bookId: String) {
        database.collection("book").document(bookId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
            }
    }


    fun insert(book: Book) {
        val ref = database.collection("book").document()
        book.bookId = ref.id
        ref.set(hashMapBook(book))
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) Log.d(TAG, "Document was added") else
                    Log.w(TAG, "Error adding document", task.exception)
            }
    }

    fun getProyekByMitraID(id: String) {
        val produkData: MutableList<Book> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Book>()
        db.collection("book")
            .orderBy("dateCreated", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val kategoriDocument = document.data["mitraId"] as String
                    if (kategoriDocument == id) {
                        val pp = document.toObject(Book::class.java)
                        pp.bookId = document.id
                        savedProdukList.add(pp)
                        produkData.add(pp)
                        Log.d(
                            TAG,
                            "getDataByUUID size : ${savedProdukList.size} getDataByUUID: $pp"
                        )
                    }
                }
                resultBookByMitraId.value = produkData
                Log.d(TAG, "readProduk size final getDataByUUID : ${savedProdukList.size}")
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents.", exception)
            }
    }

    fun uploadImage(
        context: Context?,
        portfolioId: String,
        uri: Uri?,
        fileName: String,
        callback: OnImageUploadCallback
    ) { // Investor
        var image: ByteArray? = convertUriToByteArray(context, uri)
        image = getCompressedByteArray(image, true)
        val reference: StorageReference =
            storage.reference.child("$FOLDER_BUKU/$portfolioId/$fileName")
        val uploadTask = reference.putBytes(image!!)
        uploadTask.addOnSuccessListener {
            reference.downloadUrl.addOnSuccessListener { uri1: Uri ->
                callback.onSuccess(uri1.toString())
                Log.d(TAG, "Image was uploaded")
            }
        }.addOnFailureListener { e: Exception? -> Log.w(TAG, "Error uploading image", e) }
    }

    /*fun deleteImage(imageUrl: String?) {
        if (imageUrl != null) {
            storage.getReferenceFromUrl(imageUrl).delete()
                    .addOnSuccessListener { Log.d(TAG, "Image was deleted") }
                    .addOnFailureListener { e: Exception? -> Log.w(TAG, "Error deleting image", e) }
        }
    }*/


    private fun hashMapBook(book: Book): Map<String, Any?> {
        val document: MutableMap<String, Any?> = HashMap()
        document["bookId"] = book.bookId
        document["mitraId"] = book.mitraId
        document["title"] = book.title
        document["ketersediaan"] = book.ketersediaan
        document["photo"] = book.photo
        document["description"] = book.description
        document["dateCreated"] = book.dateCreated
        document["waCount"] = book.waCount
        document["penulis"] = book.penulis

        return document
    }
}

