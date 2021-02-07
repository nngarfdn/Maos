package com.presidev.maos.mitramanagement.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.presidev.maos.R

class KatalogMitraActivity : AppCompatActivity() {

    private lateinit var bookViewModel: BookViewModel
    private lateinit var rvBookCatalog : RecyclerView
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseUser: FirebaseUser? = null

    companion object{
        private const val TAG = "KatalogMitraActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_katalog_mitra)

        bookViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(BookViewModel::class.java)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth!!.currentUser

        val fab : FloatingActionButton = findViewById(R.id.floatingActionButton)
        rvBookCatalog = findViewById(R.id.rv_bookcatalog)
        fab.setOnClickListener {
            val intent = Intent(this, AddBookActivity::class.java)
            startActivity(intent)
        }

        bookViewModel.getResultByMitraId().observe(this, { result ->
            Log.d(TAG, "onCreate: $result")
//            imgLaporanKosong.visibility = View.INVISIBLE
            val layoutManager = GridLayoutManager(this,2)
            rvBookCatalog.layoutManager = layoutManager
            val adapter = BookAdapter(result)
            rvBookCatalog.adapter = adapter
        })
    }

    override fun onStart() {
        bookViewModel.loadResultBymitraId(firebaseUser?.uid!!)
        super.onStart()
    }
    override fun onResume() {
        bookViewModel.loadResultBymitraId(firebaseUser?.uid!!)
        super.onResume()
    }
}