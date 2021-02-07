package com.presidev.maos.mitrabookcatalog.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.presidev.maos.R
import com.presidev.maos.mitramanagement.view.BookAdapter
import com.presidev.maos.mitramanagement.view.BookViewModel
import com.presidev.maos.mitramanagement.view.KatalogMitraActivity
import com.presidev.maos.profile.mitra.Mitra
import kotlinx.android.synthetic.main.activity_mitra_book_catalog.*

class MitraBookCatalogActivity : AppCompatActivity() {

    private lateinit var mitraBookViewModel: MitraBookViewModel
    private lateinit var mitra : Mitra
    companion object{
        const val EXTRA_MITRA = "extra_mitra"
        private const val TAG = "MitraBookCatalogActivit"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mitra_book_catalog)

        val intent = intent?.extras
        mitra = intent?.getParcelable<Mitra>(EXTRA_MITRA)!!

        mitraBookViewModel =  ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MitraBookViewModel::class.java)

        mitraBookViewModel.getResultByMitraId().observe(this) { result ->
            Log.d(TAG, "onCreate: $result")
//            imgLaporanKosong.visibility = View.INVISIBLE
            val layoutManager = GridLayoutManager(this, 2)
            rv_bookcatalogmitra.layoutManager = layoutManager
            val adapter = MitraBookCatalogAdapter(result)
            rv_bookcatalogmitra.adapter = adapter
        }
    }

    override fun onStart() {
        mitraBookViewModel.loadResultBymitraId(mitra.id)
        super.onStart()
    }

    override fun onResume() {
        mitraBookViewModel.loadResultBymitraId(mitra.id)
        super.onResume()
    }
}