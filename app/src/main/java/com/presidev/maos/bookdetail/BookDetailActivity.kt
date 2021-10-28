package com.presidev.maos.bookdetail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.presidev.maos.R
import com.presidev.maos.auth.preference.AuthPreference
import com.presidev.maos.auth.view.LoginActivity
import com.presidev.maos.bookdetail.healthtips.HealthTipsFragment
import com.presidev.maos.bookmark.view.BookmarkViewModel
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.mitramanagement.view.BookViewModel
import com.presidev.maos.mitramanagement.view.EditBookActivity
import com.presidev.maos.profile.user.view.UserViewModel
import com.presidev.maos.utils.AppUtils.loadImageFromUrl
import com.presidev.maos.utils.Constants
import kotlinx.android.synthetic.main.activity_book_detail.*


class BookDetailActivity : AppCompatActivity() {

    private lateinit var book: Book
    private var firebaseUser: FirebaseUser? = null
    private var isFavorite: Boolean = false
    private lateinit var favoriteViewModel: BookmarkViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var bookViewModel: BookViewModel
    companion object {
        const val EXTRA_BOOK = "extra_book"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        favoriteViewModel = ViewModelProvider(this).get(BookmarkViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser

        val intent = intent?.extras
        book = intent?.getParcelable(EditBookActivity.EXTRA_BOOK)!!

        /*setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)*/

        app_bar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) { // Collapsed
                txt_detail.text = book.title
            } else { //Expanded
                txt_detail.text = resources.getString(R.string.detail_buku)
            }
        })

        val accountPreference = AuthPreference(this)
        if (accountPreference.level != null){
            if (accountPreference.level == Constants.LEVEL_MITRA || book.ketersediaan == false ) {
                btn_peminjaman.isEnabled = false

            } else if (accountPreference.level == Constants.LEVEL_USER || book.ketersediaan == true){
                btn_peminjaman.isEnabled = true
            }
        } else {
            if (book.ketersediaan == false ) {
                btn_peminjaman.isEnabled = false
            } else if ( book.ketersediaan == true){
                btn_peminjaman.isEnabled = true
            }
        }

        val imageView = findViewById<ImageView>(R.id.img_book_detail)
        loadImageFromUrl(imageView, book.photo)
        try {
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            Palette.Builder(bitmap).generate {
                it?.let { palette ->
                    val dominantColor = palette.getDominantColor(ContextCompat.getColor(this, R.color.gray))
                    relative_detail.setBackgroundColor(dominantColor)
                    toolbar.setBackgroundColor(dominantColor)
                    img_btn.setBackgroundColor(dominantColor)
                    toolbar_layout.setBackgroundColor(dominantColor)
                    app_bar.setBackgroundColor(dominantColor)
                }
            }
        } catch (e: ClassCastException) {
            Log.e("BookDetailActivity", "Crash gambar blm selesai dimuat: $e")
        }

        if (book.ketersediaan == true) {
            txt_ketersediaan.text = "Tersedia"
            txt_ketersediaan.setTextColor(ContextCompat.getColor(this, R.color.green))
        } else {
            txt_ketersediaan.text = "Tidak Tersedia"
            txt_ketersediaan.setTextColor(ContextCompat.getColor(this, R.color.red))
        }

        txt_title.text = book.title
        txt_penulis.text = book.penulis
        txt_desc_book.text = book.description

        txt_save_contact.setOnClickListener {
            bookViewModel.loadResultBymitraId()
            val intent = Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + "${book.waCount}"))
            startActivity(intent)
        }

        img_btn.setOnClickListener { onBackPressed() }
        btn_peminjaman.setOnClickListener {

//            val args = Bundle()
////            args.putParcelable("proyek", list[position])
//            val bottomSheet = HealthTipsFragment()
////            bottomSheet.arguments = args
//            bottomSheet.show(supportFragmentManager, bottomSheet.tag)

            if (firebaseUser != null) {
                val i = Intent(this, PeminjamanActivity::class.java)
                book.waCount = book.waCount?.plus(1)
                bookViewModel.update(book)
                i.putExtra(EXTRA_BOOK, book)
                startActivity(i)
            } else {
                val i = Intent(this, LoginActivity::class.java)
                i.putExtra(EXTRA_BOOK, book)
                startActivity(i)
            }
        }

        if (firebaseUser != null) {
            // TODO : add remve favorite
            favoriteViewModel.data.observe(this, { favorite ->
                isFavorite = favorite.listBookId.contains(book.bookId)
                if (isFavorite) {
                    img_bookmark.setImageResource(R.drawable.ic_bookmark_filled)
                } else {
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
            favoriteViewModel.loadData(firebaseUser!!.uid)
            userViewModel.query(firebaseUser!!.uid)
        }
    }

}