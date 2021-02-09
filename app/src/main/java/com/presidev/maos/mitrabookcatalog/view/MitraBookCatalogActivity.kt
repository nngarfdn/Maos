package com.presidev.maos.mitrabookcatalog.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jackandphantom.blurimage.BlurImage
import com.presidev.maos.R
import com.presidev.maos.profile.mitra.Mitra
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_mitra_book_catalog.*
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


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

        Picasso.get()
                .load(mitra.logo)
                .resize(50, 50) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(img_book_catalog)

        img_book_catalog.setCornerRadius(16F)

        Picasso.get()
                .load(mitra.banner)
                .fit()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .transform(BlurTransformation(this, 25, 1))
                .into(img_cover)



        txt_mitra_name.setText(mitra.name)
        txt_mitra_description.setText(mitra.description)
        txt_address_mitra.setText(mitra.address)

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