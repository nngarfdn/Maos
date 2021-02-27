package com.presidev.maos.mitrabookcatalog.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.presidev.maos.R
import com.presidev.maos.profile.mitra.model.Mitra
import com.presidev.maos.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_information_dialog.view.*

class InformationDialog : BottomSheetDialogFragment() {

    private var mitra : Mitra? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_information_dialog, container, false)

        mitra = arguments?.getParcelable(MitraBookCatalogActivity.EXTRA_MITRA)

        AppUtils.loadProfilePicFromUrl(view.img_book_info, mitra?.logo)

        view.txt_book_title_info.text = mitra?.name
        view.txt_description_info.text = mitra?.description
        view.txt_rules.text = mitra?.rules
        return view
    }


}