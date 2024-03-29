package com.presidev.maos.bookdetail

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.PhoneNumberUtils
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.presidev.maos.R
import com.presidev.maos.auth.preference.AuthPreference
import com.presidev.maos.customview.LoadingDialog
import com.presidev.maos.membership.view.MemberCardViewModel
import com.presidev.maos.membership.view.MembershipIntroActivity
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.mitramanagement.view.EditBookActivity
import com.presidev.maos.profile.mitra.view.MitraViewModel
import com.presidev.maos.profile.user.view.UserViewModel
import com.presidev.maos.utils.AppUtils.*
import com.presidev.maos.utils.Constants.LEVEL_MITRA
import com.presidev.maos.utils.Constants.LEVEL_USER
import kotlinx.android.synthetic.main.activity_peminjaman.*
import java.io.ByteArrayOutputStream


@Suppress("DEPRECATION")
class PeminjamanActivity : AppCompatActivity(), PeminjamanCallback {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var userViewModel: UserViewModel
    private lateinit var memberCardViewModel: MemberCardViewModel
    private lateinit var mitraViewModel: MitraViewModel
    private lateinit var book: Book
    private var uriIdCard: Uri? = null
    private lateinit var loadingDialog: LoadingDialog
    private var waNumber: String? = null
    private var authPreference: AuthPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peminjaman)

        authPreference = AuthPreference(this)
        if (authPreference!!.level == LEVEL_MITRA) {
            AlertDialog.Builder(this)
                .setTitle("Tidak bisa meminjam buku")
                .setMessage("Penyedia buku tidak bisa meminjam buku")
                .setPositiveButton("Kembali") { _: DialogInterface?, _: Int -> onBackPressed() }
                .setCancelable(false)
                .create().show()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    // Explain to the user why we need to read the contacts
                    Toast.makeText(this, "Perlu akses", Toast.LENGTH_SHORT).show()
                }
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique
            }
        }

        val intent = intent?.extras
        book = intent?.getParcelable(EditBookActivity.EXTRA_BOOK)!!

        loadingDialog = LoadingDialog(this, false)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        memberCardViewModel = ViewModelProvider(this).get(MemberCardViewModel::class.java)
        mitraViewModel = ViewModelProvider(this).get(MitraViewModel::class.java)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        userViewModel.userLiveData.observe(this) { result ->
            edt_nama.setText(result.name)
            edt_alamat.setText(
                setFullAddress(
                    result.address,
                    result.province,
                    result.regency,
                    result.district
                )
            )
            if (uriIdCard != null) {
                loadImageFromUrl(imgUpload, uriIdCard.toString())
            } else {
                loadImageFromUrl(imgUpload, result.idCard)
            }

        }


        mitraViewModel.mitraLiveData.observe(this) { result ->
            waNumber = result.whatsApp
            if (!TextUtils.isDigitsOnly(waNumber)) {
                val uri = Uri.parse("http://instagram.com/_u/$waNumber")
                val likeIng = Intent(Intent.ACTION_VIEW, uri)
                likeIng.setPackage("com.instagram.android")
                try {
                    startActivity(likeIng)
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/$waNumber")
                        )
                    )
                }
                finish()
            }
        }

        loadMemberCode()

        onClickPinjam(txt_member_code.text.toString())



        btn_choose_image.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(Intent.createChooser(i, "Unggah foto"), RC_ID_CARD_IMAGE)
        }

        txt_berlangganan.setOnClickListener {
            val i = Intent(this, MembershipIntroActivity::class.java)
            startActivity(i)
        }


    }

    private fun loadMemberCode() {
        memberCardViewModel.memberCardListLiveData.observe(this) { result ->
            for (member in result) {
                if (member.mitraId == book.mitraId) {
                    txt_member_code.text = showMemberCardId(member.id)
                    onClickPinjam(member.id)
                    break
                } else {
                    txt_member_code.text = "-"
                    onClickPinjam("-")
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if (authPreference!!.level == LEVEL_USER) {
            userViewModel.query(firebaseUser.uid)
            memberCardViewModel.queryByUserId(firebaseUser.uid)
            mitraViewModel.query(book.mitraId)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_ID_CARD_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data?.data != null) {
                    uriIdCard = data.data
                    loadImageFromUrl(imgUpload, uriIdCard.toString())
                    try {
                        loadingDialog.show()
//                        val fileName: String = book.title + Calendar.getInstance().time.toString() + ".jpeg"
                        onClickPinjam(txt_member_code.text.toString())
                        loadingDialog.dismiss()
                        btn_choose_image.visibility = View.INVISIBLE
                        //    userViewModel.uploadImage(this, uriIdCard, Constants.FOLDER_ID_CARD, fileName) { imageUrl: String? ->
                        //    loadImageFromUrl(imgUpload, imageUrl)
                        //    loadMemberCode()
                        //
                        //    loadingDialog.dismiss()
                        //  }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        showToast(applicationContext, "Gagal mengunggah kartu identitas")
                        loadingDialog.dismiss()
                    }
                }
            }
        }
    }

    override fun onClickPinjam(code: String) {
        btn_simpan.setOnClickListener {
            Log.d(TAG, "onClickPinjam: $code")
            val nama = edt_nama.text.toString()
            val alamat = edt_alamat.text.toString()

            //     imgUpload.buildDrawingCache()
            //    val imgBitmap = Bitmap.createBitmap(imgUpload.getDrawingCache())
            //  val imgBitmapPathFirst = MediaStore.Images.Media.insertImage(contentResolver, imgBitmap, "title", null)
            //  val imgBitmapUriFirst = Uri.parse(imgBitmapPathFirst)

            if (edt_nama.text.toString().isEmpty() || edt_alamat.text.toString().isEmpty()) {
                Toast.makeText(this, "Lengkapi data dulu", Toast.LENGTH_SHORT).show()
            } else
                try {
                    val imgBitmap = (imgUpload.drawable as BitmapDrawable).bitmap
                    val bytes = ByteArrayOutputStream()
                    imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val imgBitmapPath = MediaStore.Images.Media.insertImage(
                        applicationContext.contentResolver,
                        imgBitmap,
                        "IMG_" + System.currentTimeMillis(),
                        null
                    )
                    val imgBitmapUri = Uri.parse(imgBitmapPath)
                    val whatsappIntent = Intent(Intent.ACTION_SEND)
                    whatsappIntent.type = "text/plain"
                    whatsappIntent.setPackage("com.whatsapp")
                    if (code == "-") {
                        whatsappIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            "Halo kak saya ingin pinjam buku yang berjudul *${book.title}* berikut data saya \n" +
                                    "Nama   : $nama \n" +
                                    "Alamat : $alamat \n" +
                                    "Terima kasih kak \n"
                        )
                    } else if (code.isNotEmpty()) {
                        whatsappIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            "Halo kak saya ingin pinjam buku yang berjudul *${book.title}* berikut data saya \n" +
                                    "Nama        : $nama \n" +
                                    "Alamat      : $alamat \n" +
                                    "Kode Member : *${code}* \n" +
                                    "Terima kasih kak \n"
                        )
                    }

                    whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri)
                    whatsappIntent.putExtra(
                        "jid",
                        PhoneNumberUtils.stripSeparators(waNumber) + "@s.whatsapp.net"
                    )
                    whatsappIntent.type = "image/jpeg"
                    whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    try {
                        startActivity(whatsappIntent)
                    } catch (ex: ActivityNotFoundException) {
                        Toast.makeText(this, "Whatsapp belum terinstall", Toast.LENGTH_SHORT).show()
                    } catch (e: ClassCastException) {
                        Toast.makeText(this, "Kartu identitas belum ada", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: ClassCastException) {
                    Log.e("BookDetailActivity", "Crash gambar blm selesai dimuat: $e")
                    Toast.makeText(this, "Kartu identitas belum ada", Toast.LENGTH_SHORT).show()
                }
        }
    }

    companion object {
        private const val TAG = "PeminjamanActivity"
        private const val RC_ID_CARD_IMAGE = 100
        private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100
    }
}