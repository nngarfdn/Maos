package com.presidev.maos.mitramanagement.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.presidev.maos.R
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.utils.AppUtils
import com.squareup.picasso.Picasso
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

        bookViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(BookViewModel::class.java)

        val intent = intent?.extras
        book = intent?.getParcelable<Book>(EXTRA_BOOK)!!

        edt_title.setText(book?.title)
        edt_description.setText(book?.description)
        edt_sinopsis.setText(book?.sinopsis)
        swtKetersediaan.isChecked = book?.ketersediaan!!

        Picasso.get()
                .load(book?.photo)
                .resize(100, 100) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(imgUpload)

        btn_choose_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Unggah bukti pembayaran"), RC_PAYMENT_IMAGE)
        }


//        btn_simpan.setOnClickListener {
//
//            val id = book.bookId
//            val judul = edt_title.text.toString()
//            val deskripsi = edt_description.text.toString()
//            val sinopsis = edt_sinopsis.text.toString()
//            val switchState = swtKetersediaan.isChecked()
//
//            bookViewModel.update(book)
//            val intentResult = Intent()
////                intentResult.putExtra(EXTRA_PAYMENT, payment)
//            setResult(RC_ADD_PAYMENT, intentResult)
////                loadingDialog.dismiss()
//            AppUtils.showToast(applicationContext, "Berhasil")
//            finish()
//
//            AppUtils.showToast(applicationContext, "Tunggu Sebentar...")
////            loadingDialog.show()
//
//            val fileName: String = book.title + Calendar.getInstance().time + ".jpeg"
//
//            if (fileName != book.photo) {
//            bookViewModel.uploadImage(this, book.bookId, uriPaymentImage, fileName) { imageUrl ->
//                book.photo = imageUrl
//                book.mitraId = id
//                book.title = judul
//                book.ketersediaan = switchState
//                book.description = deskripsi
//                book.sinopsis = sinopsis
//                bookViewModel.update(book)
//                val intentResult = Intent()
////                intentResult.putExtra(EXTRA_PAYMENT, payment)
//                setResult(RC_ADD_PAYMENT, intentResult)
////                loadingDialog.dismiss()
//                AppUtils.showToast(applicationContext, "Berhasil")
//                finish()
//            } }else {
//                book.mitraId = id
//                book.title = judul
//                book.ketersediaan = switchState
//                book.description = deskripsi
//                book.sinopsis = sinopsis
//                bookViewModel.update(book)
//                AppUtils.showToast(applicationContext, "Berhasil")
//                finish()
//            }
//        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_PAYMENT_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) if (data.data != null) {
                    uriPaymentImage = data.data
                    Picasso.get()
                            .load(uriPaymentImage.toString())
                            .resize(100, 100) // resizes the image to these dimensions (in pixel)
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher)
                            .into(imgUpload)
                    btn_simpan.setEnabled(true)
                }
            }
        }
    }

}