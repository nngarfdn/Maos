package com.presidev.maos.borrowbook

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.presidev.maos.R
import com.presidev.maos.mitramanagement.model.Book
import com.presidev.maos.mitramanagement.view.EditBookActivity
import com.presidev.maos.profile.user.UserViewModel
import com.presidev.maos.subscribe.viewmodel.MemberCardViewModel
import com.presidev.maos.utils.AppUtils
import com.presidev.maos.utils.AppUtils.loadImageFromUrl
import kotlinx.android.synthetic.main.activity_peminjaman.*

class PeminjamanActivity : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var userViewModel: UserViewModel
    private lateinit var memberCardViewModel: MemberCardViewModel
    private lateinit var book :  Book
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peminjaman)

        val intent = intent?.extras
        book = intent?.getParcelable(EditBookActivity.EXTRA_BOOK)!!

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        memberCardViewModel = ViewModelProvider(this).get(MemberCardViewModel::class.java)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        edt_member_code.setFocusable(false)

        userViewModel.userLiveData.observe(this) { result ->
            edt_nama.setText(result.name)
            edt_alamat.setText(result.address)
            loadImageFromUrl(imgUpload, result.idCard)
        }


        memberCardViewModel.memberCardListLiveData.observe(this) { result ->
           for (member in result){
               if (member.mitraId.equals(book.mitraId)){
                   edt_member_code.setText(member.id)
               }
           }
        }
    }

    override fun onStart() {
        super.onStart()
        userViewModel.query(firebaseUser.uid)
        memberCardViewModel.queryByUserId(firebaseUser.uid)

    }
}