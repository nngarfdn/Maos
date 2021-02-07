package com.presidev.maos.dashboard.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.presidev.maos.R
import com.presidev.maos.mitramanagement.view.BookAdapter
import com.presidev.maos.mitramanagement.view.BookViewModel
import com.presidev.maos.mitramanagement.view.KatalogMitraActivity
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    private lateinit var  dashboardViewModel: DashboardViewModel

    companion object{
        private const val TAG = "DashboardFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        dashboardViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DashboardViewModel::class.java)

        dashboardViewModel.getResultMitra().observe(viewLifecycleOwner, { result ->
            Log.d(TAG, "onCreate: $result")
//            imgLaporanKosong.visibility = View.INVISIBLE
            val layoutManager =  LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
            rv_mitra.layoutManager = layoutManager
            val adapter = DashboardMitraAdapter(result)
            rv_mitra.adapter = adapter
        })
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        dashboardViewModel.loadResultMitra()
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        dashboardViewModel.loadResultMitra()
    }
}