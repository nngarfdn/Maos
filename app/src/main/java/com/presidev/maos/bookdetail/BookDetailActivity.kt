package com.presidev.maos.bookdetail

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.presidev.maos.R
import com.presidev.maos.bookmark.view.BookmarkViewModel
import com.presidev.maos.borrowbook.PeminjamanActivity
import com.presidev.maos.login.view.LoginActivity
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.mitramanagement.view.EditBookActivity
import com.presidev.maos.utils.AppUtils
import kotlinx.android.synthetic.main.activity_book_detail.*

class BookDetailActivity : AppCompatActivity() {

    private lateinit var book: Book
    private var firebaseUser: FirebaseUser? = null
    var isFavorite: Boolean = false
    private lateinit var favoriteViewModel: BookmarkViewModel

    companion object {
        const val EXTRA_BOOK = "extra_book"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        favoriteViewModel = ViewModelProviders.of(this).get(BookmarkViewModel::class.java)

        val intent = intent?.extras
        book = intent?.getParcelable(EditBookActivity.EXTRA_BOOK)!!


        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser


        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val imageView = findViewById<ImageView>(R.id.img_book_detail)

        AppUtils.loadImageFromUrl(imageView, book?.photo)

        try {
            var bitmap = (imageView.drawable as BitmapDrawable).bitmap
            Palette.Builder(bitmap).generate {
                it?.let { palette ->
                    val dominantColor = palette.getDominantColor(ContextCompat.getColor(this, R.color.gray))
                    relative_detail.setBackgroundColor(dominantColor)
                    toolbar.setBackgroundColor(dominantColor)
                    img_btn.setBackgroundColor(dominantColor)
                    toolbar_layout.setBackgroundColor(dominantColor)
                }
            }
        } catch (e: ClassCastException) {
            Log.e("BookDetailActivity", "Crash gambar blm selesai dimuat: " + e)
        }

        if (book.ketersediaan == true) {
            txt_ketersediaan.text = "Tersedia"
            txt_ketersediaan.setTextColor(ContextCompat.getColor(this, R.color.green))
        } else {
            txt_ketersediaan.text = "Tidak Tersedia"
            txt_ketersediaan.setTextColor(ContextCompat.getColor(this, R.color.red))
        }

        txt_title.setText(book.title)
        txt_penulis.setText(book.penulis)
        txt_desc_book.setText(book.description)


        img_btn.setOnClickListener { onBackPressed() }
        btn_peminjaman.setOnClickListener {
            if (firebaseUser != null) {
                val intent = Intent(this, PeminjamanActivity::class.java)
                intent.putExtra(EXTRA_BOOK, book)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra(EXTRA_BOOK, book)
                startActivity(intent)
            }
        }

        if (firebaseUser != null) {
            // TODO : add remve favorite
            favoriteViewModel.data.observe(this, Observer { favorite ->
                isFavorite = favorite.listBookId.contains(book.bookId)
                if (isFavorite){
                    img_bookmark.setImageResource(R.drawable.ic_bookmark_filled)
                }else {
                    img_bookmark.setImageResource(R.drawable.ic_bookmark_outline)
                }
            })
            img_bookmark.setOnClickListener {
                val userId = firebaseUser!!.uid
                if (isFavorite) {
                    favoriteViewModel.remove(userId, book.bookId)
                    img_bookmark.setImageResource(R.drawable.ic_bookmark_outline)
                    Toast.makeText(this, "Berhasil Hapus Bookmark", Toast.LENGTH_SHORT).show()
                } else {
                    favoriteViewModel.add(userId, book.bookId)
                    img_bookmark.setImageResource(R.drawable.ic_bookmark_filled)
                    Toast.makeText(this, "Berhasil Tambah Bookmark", Toast.LENGTH_SHORT).show()
                }
                isFavorite = !isFavorite
            }

        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        if (firebaseUser != null) {
            // TODO : add remve favorite
            favoriteViewModel.loadData(firebaseUser!!.uid)

        }
    }

}