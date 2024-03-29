package com.presidev.maos.bookdetail.healthtips

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.presidev.maos.R

class HealthTipsFragment : BottomSheetDialogFragment() {

    private var dotsLayout: LinearLayout? = null
    private var layouts: ArrayList<Int> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_health_tips, container, false)
        val viewPager: ViewPager = view.findViewById(R.id.view_pager)
        dotsLayout = view.findViewById(R.id.layoutDots)

        // layout xml slide 1 sampai 4
        // add few more layouts if you want

        // layout xml slide 1 sampai 4
        // add few more layouts if you want
        layouts = arrayListOf(
            R.layout.fragment_a,
//                R.layout.fragment_b,
            R.layout.fragment_c
        )

        // tombol dots (lingkaran kecil perpindahan slide)

        // tombol dots (lingkaran kecil perpindahan slide)
        addBottomDots(0)
        return view;
    }

    private fun addBottomDots(currentPage: Int) {
        val dots = arrayOfNulls<TextView>(layouts.size)
        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
        dotsLayout!!.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(context)
            dots[i]!!.text = Html.fromHtml("&#8226;")
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(colorsInactive[currentPage])
            dotsLayout!!.addView(dots[i])
        }
        if (dots.isNotEmpty()) dots[currentPage]!!.setTextColor(colorsActive[currentPage])
    }
}