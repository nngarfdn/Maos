package com.presidev.maos.catatanku.reminder.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.presidev.maos.catatanku.reminder.model.Reminder
import java.util.*

class ReminderRepository {

    private val database = FirebaseFirestore.getInstance()

    private var resultReminder: MutableLiveData<List<Reminder>> = MutableLiveData()
    fun getResultsReminder(): LiveData<List<Reminder>> = resultReminder

    companion object{
        private const val TAG = "ReminderRepository"
    }


    fun getReminder(id: String) {
        val produkData: MutableList<Reminder> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Reminder>()
        db.collection("reminder")
                .orderBy("isKembali", Query.Direction.ASCENDING)
                .orderBy("returnDate", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val kategoriDocument = document.data["uuid"] as String
                        if (kategoriDocument == id) {
                            val pp = document.toObject(Reminder::class.java)
                            pp.id = document.id
                            savedProdukList.add(pp)
                            produkData.add(pp)
                            Log.d(TAG, "Reminder size : ${savedProdukList.size} Reminder: $pp")
                        }
                    }
                    resultReminder.value = produkData
                    Log.d(TAG, "readProduk size final Reminder : ${savedProdukList.size}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
    }

    fun update(reminder: Reminder) {
        val idProduk = reminder.id
        val item = hashMapBook(reminder)

        if (idProduk != null) {
            database.collection("reminder").document(idProduk)
                    .set(item)
                    .addOnSuccessListener {
                        Log.d(TAG, "Succes update")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error adding document", e)
                    }
        }
    }

    fun insert(reminder: Reminder) {
        val ref = database.collection("reminder").document()
        reminder.id = ref.id
        ref.set(hashMapBook(reminder))
                .addOnCompleteListener { task: Task<Void?> ->
                    if (task.isSuccessful) Log.d(TAG, "Document was added") else
                        Log.w(TAG, "Error adding document", task.exception) }
    }

    fun delete(bookId: String) {
        database.collection("reminder").document(bookId)
                .delete()
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error deleting document", e)
                }
    }

    private fun hashMapBook(reminder : Reminder): Map<String, Any?> {
        val document: MutableMap<String, Any?> = HashMap()
        document["id"] = reminder.id
        document["uuid"] = reminder.uuid
        document["bookTitle"] = reminder.bookTitle
        document["tempatPeminjam"] = reminder.tempatPeminjam
        document["returnDate"] = reminder.returnDate
        document["isKembali"] = reminder.isKembali

        return document
    }
}