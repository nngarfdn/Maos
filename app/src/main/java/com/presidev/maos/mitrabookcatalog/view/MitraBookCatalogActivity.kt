package com.presidev.maos.mitrabookcatalog.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.presidev.maos.R
import com.presidev.maos.profile.mitra.model.Mitra
import com.presidev.maos.utils.AppUtils.*
import kotlinx.android.synthetic.main.activity_mitra_book_catalog.*


class MitraBookCatalogActivity : AppCompatActivity(){

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
        mitra = intent?.getParcelable(EXTRA_MITRA)!!

        loadProfilePicFromUrl(img_book_catalog, mitra.logo)

        img_book_catalog.cornerRadius = 16F

        loadBlurImageFromUrl(this, img_cover, mitra.banner)

        txt_mitra_name.text = mitra.name
        txt_mitra_description.text = mitra.description
        txt_address_mitra.text = setFullAddress(mitra.address, mitra.province, mitra.regency, mitra.district)

        mitraBookViewModel =  ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MitraBookViewModel::class.java)

        mitraBookViewModel.getResultByMitraId().observe(this) { result ->
            Log.d(TAG, "onCreate: $result")
            val layoutManager = GridLayoutManager(this, 2)
            rv_bookcatalognew.layoutManager = layoutManager
            val adapter = MitraBookCatalogAdapter(result)
            rv_bookcatalognew.adapter = adapter
        }

        mitraBookViewModel.getResultByMitraIdPopuler().observe(this) { result ->
            Log.d(TAG, "onCreate: $result")
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL ,false)
            rv_bookpopuler.layoutManager = layoutManager
            val adapter = MitraBookCatalogAdapter(result)
            rv_bookpopuler.adapter = adapter
        }

        txt_mitra_description.setOnClickListener { img_mitra_information.performClick() }
        img_mitra_information.setOnClickListener {
            val args = Bundle()
            args.putParcelable(EXTRA_MITRA, mitra)
            val bottomSheet = InformationDialog()
            bottomSheet.arguments = args
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }



    override fun onStart() {
        mitraBookViewModel.loadResultBymitraId(mitra.id)
        mitraBookViewModel.loadResultBymitraIdPopuler(mitra.id)
        super.onStart()
    }

    override fun onResume() {
        mitraBookViewModel.loadResultBymitraId(mitra.id)
        mitraBookViewModel.loadResultBymitraIdPopuler(mitra.id)
        super.onResume()
    }
}