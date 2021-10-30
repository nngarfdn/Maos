package com.presidev.maos.mitramanagement.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.presidev.maos.R
import com.presidev.maos.customview.LoadingDialog
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.utils.AppUtils.*
import kotlinx.android.synthetic.main.layout_add_update_buku.*
import java.sql.Timestamp
import java.util.*

class AddBookActivity : AppCompatActivity() {

    companion object{
        private const val RC_PICK_IMAGE = 100
    }

    private var uriImage: Uri? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseUser: FirebaseUser? = null
    private var loadingDialog: LoadingDialog? = null

    private lateinit var bookViewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        bookViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(BookViewModel::class.java)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth!!.currentUser
        loadingDialog = LoadingDialog(this, false)

        swtKetersediaan.isChecked = true

        btn_choose_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Unggah foto"), RC_PICK_IMAGE)
        }

        btn_simpan.setOnClickListener {
            val book = Book()
            val id = firebaseUser?.uid ?: "-1"
            val judul = edt_title.text.toString()
            val deskripsi = edt_description.text.toString()
            val penulis = edt_penulis.text.toString()
            val switchState = swtKetersediaan.isChecked
            val fileName: String = book.title + Calendar.getInstance().time + ".jpeg"

            if (judul.isEmpty() || deskripsi.isEmpty() || penulis.isEmpty() || uriImage == null) {
                showToast(applicationContext, "Pastikan semua data lengkap")
                return@setOnClickListener
            }

            loadingDialog!!.show()
            bookViewModel.uploadImage(this, book.bookId, uriImage, fileName) { imageUrl ->
                book.photo = imageUrl
                book.mitraId = id
                book.title = judul
                book.ketersediaan = switchState
                book.description = deskripsi
                book.penulis = penulis
                book.dateCreated = Timestamp(System.currentTimeMillis()).toString()
                bookViewModel.insert(book)
                loadingDialog!!.dismiss()
                showToast(applicationContext, "Berhasil")
                finish()
            }
        }

        edt_description.setOnTouchListener(scrollableListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) if (data.data != null) {
                    uriImage = data.data
                    loadImageFromUrl(imgUpload, uriImage.toString())
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}