package com.presidev.maos.bookmark.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.presidev.maos.bookmark.model.Bookmark
import java.util.*

class BookmarkRepository {

    private val database = FirebaseFirestore.getInstance()
    private val resultData = MutableLiveData<Bookmark>()

    companion object {
        private const val TAG = "BookmarkRepository"
    }

    val data: LiveData<Bookmark>
        get() = resultData

    fun query(userId: String) {
        database.collection("bookmark").document(userId)
            .get()
            .addOnCompleteListener { task: Task<DocumentSnapshot> ->
                if (task.isSuccessful) {
                    var favorite = task.result?.toObject(Bookmark::class.java)
                    // Pengguna yang belum pernah tambah favorit
                    if (favorite == null) {
                        favorite = Bookmark(ArrayList())
                        insert(userId, favorite) // Sekalian bikin dokumennya
                    }
                    resultData.postValue(favorite)
                } else Log.w(TAG, "Error querying document", task.exception)
            }
    }

    private fun insert(userId: String, favorite: Bookmark) {
        database.collection("bookmark").document(userId)
            .set(favorite)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) Log.d(
                    TAG,
                    "Document was added"
                ) else Log.w(TAG, "Error adding document", task.exception)
            }
    }

    fun add(userId: String?, productId: String?) {
        database.collection("bookmark").document(userId!!)
            .update("listBookId", FieldValue.arrayUnion(productId))
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) Log.d(
                    TAG,
                    "Document was updated"
                ) else Log.w(TAG, "Error updating document", task.exception)
            }
    }

    fun remove(userId: String?, productId: String?) {
        database.collection("bookmark").document(userId!!)
            .update("listBookId", FieldValue.arrayRemove(productId))
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) Log.d(
                    TAG,
                    "Document was updated"
                ) else Log.w(TAG, "Error updating document", task.exception)
            }
    }
}