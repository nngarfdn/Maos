package com.presidev.maos.mitramanagement.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Timestamp.now
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.type.DateTime
import com.presidev.maos.R
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.utils.AppUtils
import com.presidev.maos.utils.AppUtils.loadImageFromUrl
import com.presidev.maos.utils.AppUtils.showToast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_add_update_buku.*
import java.sql.Timestamp
import java.util.*

class AddBookActivity : AppCompatActivity() {

    private val RC_PAYMENT_IMAGE = 100
    val RC_ADD_PAYMENT = 200
    private var uriPaymentImage: Uri? = null

    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseUser: FirebaseUser? = null



    private lateinit var bookViewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book_activity)

        bookViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(BookViewModel::class.java)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth!!.getCurrentUser()

        swtKetersediaan.isChecked = true


        btn_choose_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Unggah bukti pembayaran"), RC_PAYMENT_IMAGE)
        }


        btn_simpan.setOnClickListener {
            val judul = edt_title.text.toString()
            val deskripsi = edt_description.text.toString()
            val penulis = edt_penulis.text.toString()
            val switchState = swtKetersediaan.isChecked()
//            loadingDialog.show()


            if (judul.isEmpty() || deskripsi.isEmpty() || penulis.isEmpty()){
                showToast(applicationContext, "Pastikan semua data lengkap")
            }

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
                        val book = Book()
                        val id = firebaseUser?.uid
                        val judul = edt_title.text.toString()
                        val deskripsi = edt_description.text.toString()
                        val penulis = edt_penulis.text.toString()
                        val switchState = swtKetersediaan.isChecked()
                        val fileName: String = book.title + Calendar.getInstance().time + ".jpeg"

                        bookViewModel.uploadImage(this, book.bookId, uriPaymentImage, fileName) { imageUrl ->
                            if (judul.isEmpty() || deskripsi.isEmpty() || penulis.isEmpty() || fileName.isEmpty()) {
                                AppUtils.showToast(applicationContext, "Pastikan semua data lengkap")
                            } else {
                                book.photo = imageUrl
                                book.mitraId = id
                                book.title = judul
                                book.ketersediaan = switchState
                                book.description = deskripsi
                                book.penulis = penulis
                                book.dateCreated = Timestamp(System.currentTimeMillis()).toString()
                                bookViewModel.insert(book)
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
}