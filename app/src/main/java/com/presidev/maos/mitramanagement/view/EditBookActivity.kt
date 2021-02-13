package com.presidev.maos.mitramanagement.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.presidev.maos.R
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.utils.AppUtils
import com.presidev.maos.utils.AppUtils.loadImageFromUrl
import kotlinx.android.synthetic.main.activity_edit_book.*
import kotlinx.android.synthetic.main.layout_add_update_buku.*
import java.util.*

class EditBookActivity : AppCompatActivity() {

    companion object {
        val EXTRA_BOOK = "extra_book"
    }

    private val RC_PAYMENT_IMAGE = 100
    val RC_ADD_PAYMENT = 200
    private var uriPaymentImage: Uri? = null

    private lateinit var book: Book
    private lateinit var bookViewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        bookViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(BookViewModel::class.java)

        val intent = intent?.extras
        book = intent?.getParcelable<Book>(EXTRA_BOOK)!!

        edt_title.setText(book?.title)
        edt_description.setText(book?.description)
        edt_penulis.setText(book?.penulis)
        swtKetersediaan.isChecked = book?.ketersediaan!!

        loadImageFromUrl(imgUpload, book?.photo)

        btn_choose_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Unggah bukti pembayaran"), RC_PAYMENT_IMAGE)
        }

        btn_simpan.setOnClickListener {
            val id = book.bookId
            val judul = edt_title.text.toString()
            val deskripsi = edt_description.text.toString()
            val sinopsis = edt_penulis.text.toString()
            val switchState = swtKetersediaan.isChecked()

            book.bookId = id
            book.mitraId = book.mitraId
            book.title = judul
            book.ketersediaan = switchState
            book.description = deskripsi
            book.penulis = sinopsis

            if (judul.isEmpty() || deskripsi.isEmpty() || sinopsis.isEmpty()){
                AppUtils.showToast(applicationContext, "Pastikan semua data lengkap")
            }else {
                bookViewModel.update(book)
                AppUtils.showToast(applicationContext, "Berhasil")
                finish()
            }

        }

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


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_PAYMENT_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) if (data.data != null) {
                    uriPaymentImage = data.data
                    loadImageFromUrl(imgUpload, uriPaymentImage.toString())

                    btn_simpan.setOnClickListener {
                        val id = book.bookId
                        val judul = edt_title.text.toString()
                        val deskripsi = edt_description.text.toString()
                        val penulis = edt_penulis.text.toString()
                        val switchState = swtKetersediaan.isChecked()
                        val fileName: String = book.title + Calendar.getInstance().time + ".jpeg"

                        bookViewModel.uploadImage(this, book.bookId, uriPaymentImage, fileName) { imageUrl ->
                            if (judul.isEmpty() || deskripsi.isEmpty() || penulis.isEmpty() || fileName.isEmpty()){
                                AppUtils.showToast(applicationContext, "Pastikan semua data lengkap")
                            }else {
                                book.photo = imageUrl
                                book.bookId = id
                                book.mitraId = book.mitraId
                                book.title = judul
                                book.ketersediaan = switchState
                                book.description = deskripsi
                                book.penulis = penulis
                                bookViewModel.update(book)
                                val intentResult = Intent()
//                intentResult.putExtra(EXTRA_PAYMENT, payment)
                                setResult(RC_ADD_PAYMENT, intentResult)
//                loadingDialog.dismiss()
                                AppUtils.showToast(applicationContext, "Berhasil")
                                finish()
                            }

                    }

                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}