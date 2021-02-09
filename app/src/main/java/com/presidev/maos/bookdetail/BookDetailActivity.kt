package com.presidev.maos.bookdetail

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.presidev.maos.R
import com.presidev.maos.borrowbook.PeminjamanActivity
import com.presidev.maos.login.view.LoginActivity
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.mitramanagement.view.EditBookActivity
import com.presidev.maos.utils.AppUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.layout_add_update_buku.*


class BookDetailActivity : AppCompatActivity() {

    private lateinit var book: Book
    private var firebaseUser: FirebaseUser? = null

    companion object{
        const val EXTRA_BOOK = "extra_book"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        val intent = intent?.extras
        book = intent?.getParcelable(EditBookActivity.EXTRA_BOOK)!!

        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser


        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        val imageView  = findViewById<ImageView>(R.id.img_book_detail)

        AppUtils.loadImageFromUrl(imageView, book?.photo)

        var bitmap = (imageView.drawable as BitmapDrawable).bitmap

        Palette.Builder(bitmap).generate { it?.let { palette ->
            val dominantColor = palette.getDominantColor(ContextCompat.getColor(this, R.color.gray))
            val  darkMuted = palette.getDarkMutedColor(ContextCompat.getColor(this, R.color.gray_dark))
            val vibran = palette.getLightVibrantColor(ContextCompat.getColor(this, R.color.gray_dark))
            relative_detail.setBackgroundColor(dominantColor)
            toolbar.setBackgroundColor(dominantColor)
            img_btn.setBackgroundColor(dominantColor)
            toolbar_layout.setBackgroundColor(dominantColor)

            // TODO: use dominant color
        } }



        if (book.ketersediaan == true){
            txt_ketersediaan.text = "Tersedia"
            txt_ketersediaan.setTextColor(ContextCompat.getColor(this, R.color.green))
        } else {
            txt_ketersediaan.text = "Tidak Tersedia"
            txt_ketersediaan.setTextColor(ContextCompat.getColor(this, R.color.red))
        }

        txt_title.setText(book.title)
        txt_penulis.setText(book.penulis)
        txt_desc_book.setText(book.description)


        img_btn.setOnClickListener{onBackPressed()}
        btn_peminjaman.setOnClickListener {
            if (firebaseUser != null) {
                val intent = Intent(this, PeminjamanActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}