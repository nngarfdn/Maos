package com.presidev.maos.mitramanagement.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.presidev.maos.R
import com.presidev.maos.customview.LoadingDialog
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.utils.AppUtils.*
import kotlinx.android.synthetic.main.activity_edit_book.*
import kotlinx.android.synthetic.main.layout_add_update_buku.*
import java.util.*

class EditBookActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_BOOK = "extra_book"
        private const val RC_PICK_IMAGE = 100
    }

    private var uriImage: Uri? = null

    private lateinit var book: Book
    private lateinit var bookViewModel: BookViewModel
    private var loadingDialog: LoadingDialog? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        loadingDialog = LoadingDialog(this, false)
        bookViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(BookViewModel::class.java)

        val intent = intent?.extras
        book = intent?.getParcelable(EXTRA_BOOK)!!

        edt_title.setText(book.title)
        edt_description.setText(book.description)
        edt_penulis.setText(book.penulis)
        swtKetersediaan.isChecked = book.ketersediaan!!

        loadImageFromUrl(imgUpload, book.photo)

        btn_choose_image.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(Intent.createChooser(i, "Unggah foto"), RC_PICK_IMAGE)
        }

        btn_simpan.setOnClickListener {
            val id = book.bookId
            val judul = edt_title.text.toString()
            val deskripsi = edt_description.text.toString()
            val penulis = edt_penulis.text.toString()
            val switchState = swtKetersediaan.isChecked
            val fileName: String = book.title + Calendar.getInstance().time + ".jpeg"

            if (judul.isEmpty() || deskripsi.isEmpty() || penulis.isEmpty()){
                showToast(applicationContext, "Pastikan semua data lengkap")
                return@setOnClickListener
            }

            book.bookId = id
            book.mitraId = book.mitraId
            book.title = judul
            book.ketersediaan = switchState
            book.description = deskripsi
            book.penulis = penulis

            if (uriImage == null){
                bookViewModel.update(book)
                loadingDialog?.dismiss()
                showToast(applicationContext, "Berhasil")
                finish()
            } else{
                loadingDialog?.show()
                bookViewModel.uploadImage(this, book.bookId, uriImage, fileName) { imageUrl ->
                    book.photo = imageUrl
                    bookViewModel.update(book)
                    loadingDialog?.dismiss()
                    showToast(applicationContext, "Berhasil")
                    finish()
                }
            }
        }

        btn_delete.text = "Hapus"

        btn_delete.setOnClickListener {
            AlertDialog.Builder(this)
                    .setTitle("Hapus data buku")
                    .setMessage("Apakah kamu yakin ingin menghapus?")
                    .setNegativeButton("Tidak", null)
                    .setPositiveButton("Ya") { _, _ ->
                        book.bookId?.let { it1 -> bookViewModel.delete(it1) }
                        finish()
                    }.create().show()
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