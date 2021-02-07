package com.presidev.maos.bookdetail

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.presidev.maos.R
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.mitramanagement.view.EditBookActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_book_detail.*


class BookDetailActivity : AppCompatActivity() {

    private lateinit var book: Book

    companion object{
        const val EXTRA_BOOK = "extra_book"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        val intent = intent?.extras
        book = intent?.getParcelable<Book>(EditBookActivity.EXTRA_BOOK)!!

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        val imageView  = findViewById<ImageView>(R.id.img_book_detail)

        Picasso.get()
                .load(book.photo)
                .resize(120, 200) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView)

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

        txt_title.setText(book.title)
        txt_penulis.setText(book.penulis)

        txt_desc_book.setText(book.description)

        img_btn.setOnClickListener{onBackPressed()}


    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}