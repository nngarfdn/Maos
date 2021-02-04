package com.presidev.maos.mitra.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.presidev.maos.R
import kotlinx.android.synthetic.main.activity_katalog_mitra.*

class KatalogMitraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_katalog_mitra)

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddBookActiviity::class.java)
            startActivity(intent)
        }

    }
}