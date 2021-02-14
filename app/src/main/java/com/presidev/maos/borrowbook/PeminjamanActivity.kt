package com.presidev.maos.borrowbook

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.presidev.maos.R
import com.presidev.maos.customview.LoadingDialog
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.mitramanagement.view.EditBookActivity
import com.presidev.maos.profile.mitra.MitraViewModel
import com.presidev.maos.profile.user.UserViewModel
import com.presidev.maos.subscribe.view.MembershipIntroActivity
import com.presidev.maos.subscribe.viewmodel.MemberCardViewModel
import com.presidev.maos.utils.AppUtils
import com.presidev.maos.utils.AppUtils.*
import com.presidev.maos.utils.Constants
import kotlinx.android.synthetic.main.activity_peminjaman.*
import java.io.ByteArrayOutputStream
import java.util.*


class PeminjamanActivity : AppCompatActivity(), PeminjamanCallback {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var userViewModel: UserViewModel
    private lateinit var memberCardViewModel: MemberCardViewModel
    private lateinit var mitraViewModel: MitraViewModel
    private lateinit var book :  Book
    private var uriIdCard: Uri? = null
    private lateinit var loadingDialog: LoadingDialog
    private var waNumber : String? = null

    private val RC_PROFILE_IMAGE = 100
    private val RC_ID_CARD_IMAGE = 200
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peminjaman)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to read the contacts
                }
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique
                return
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
            edt_alamat.setText(result.address)
            loadImageFromUrl(imgUpload, result.idCard)
            loadMemberCode()
        }


        mitraViewModel.mitraLiveData.observe(this){result ->
            waNumber = result.whatsApp

        }

        loadMemberCode()

        btn_choose_image.setOnClickListener {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "Ambil foto identitas")
            values.put(MediaStore.Images.Media.DESCRIPTION, "Menggunakan kamera")
            uriIdCard = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            val intentIdCard = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intentIdCard.putExtra(MediaStore.EXTRA_OUTPUT, uriIdCard)
            startActivityForResult(intentIdCard, RC_ID_CARD_IMAGE)
        }

        txt_berlangganan.setOnClickListener {
            val intent = Intent(this, MembershipIntroActivity::class.java)
            startActivity(intent)
        }

        edt_alamat.setOnTouchListener(scrollableListener)
    }

    private fun loadMemberCode() {

        memberCardViewModel.memberCardListLiveData.observe(this) { result ->
            for (member in result) {
                if (member.mitraId.equals(book.mitraId)) {
                    txt_member_code.setText(member.id)
                    onClickPinjam(member.id)
                    break
                } else {
                    txt_member_code.setText("-")
                    onClickPinjam("-")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        userViewModel.query(firebaseUser.uid)
        memberCardViewModel.queryByUserId(firebaseUser.uid)
        mitraViewModel.query(book.mitraId)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_ID_CARD_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    loadingDialog.show()

                    val fileName: String = book.title + Calendar.getInstance().time.toString() + ".jpeg"
                    userViewModel.uploadImage(this, uriIdCard, Constants.FOLDER_ID_CARD, fileName) { imageUrl: String? ->
                        loadImageFromUrl(imgUpload, imageUrl)

                        loadMemberCode()

                        loadingDialog.dismiss()


                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                    AppUtils.showToast(applicationContext, "Gagal mengunggah kartu identitas")
                    loadingDialog.dismiss()
                }
            }
        }
    }

    override fun onClickPinjam(code: String) {
        btn_simpan.setOnClickListener{

            Log.d(TAG, "onClickPinjam: $code")
            val nama = edt_nama.text.toString()
            val alamat = edt_alamat.text.toString()

            val code = txt_member_code.text.toString()

            try {
                val bitmap = ((findViewById<View>(R.id.imgUpload) as ImageView).drawable as BitmapDrawable).bitmap
                val bytes = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val imgBitmapPath = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "title", null)
                val imgBitmapUri = Uri.parse(imgBitmapPath)
                val whatsappIntent = Intent(Intent.ACTION_SEND)
                whatsappIntent.type = "text/plain"
                whatsappIntent.setPackage("com.whatsapp")
                if (code.equals("-")){
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT,
                            "Halo kak saya ingin pinjam buku yang berjudul *${book.title}* berikut data saya \n" +
                                    "Nama   : $nama \n" +
                                    "Alamat : $alamat \n" +
                                    "Terima kasih kak \n")
                }else {
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT,
                            "Halo kak saya ingin pinjam buku yang berjudul *${book.title}* berikut data saya \n" +
                                    "Nama        : $nama \n" +
                                    "Alamat      : $alamat \n" +
                                    "Kode Member : *${code}* \n" +
                                    "Terima kasih kak \n")
                }

                whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri)
                whatsappIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(waNumber) + "@s.whatsapp.net");
                whatsappIntent.type = "image/jpeg"
                whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                try {
                    startActivity(whatsappIntent)
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(this, "WhatsApp belum terinstall", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ClassCastException) {
                Log.e(TAG, "getDrawable: " + e)
                showToast(this, "Kartu identitas belum ada")
            }
        }
    }

    companion object{
        private const val TAG = "PeminjamanActivity"
    }
}