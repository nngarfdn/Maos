package com.presidev.maos.home

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.presidev.maos.R
import com.presidev.maos.auth.preference.AuthPreference
import com.presidev.maos.utils.AppUtils.hideStatusBar
import com.presidev.maos.utils.Constants.EXTRA_LEVEL
import me.ibrahimsn.lib.SmoothBottomBar


class App : AppCompatActivity() {
    private var firebaseUser: FirebaseUser? = null
    private var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar(this, supportActionBar)
        setContentView(R.layout.activity_app)

        val pagerAdapter: MainPagerAdapter = if (intent.hasExtra(EXTRA_LEVEL)) MainPagerAdapter(
            intent.getStringExtra(EXTRA_LEVEL),
            supportFragmentManager
        )
        else MainPagerAdapter(AuthPreference(this).level, supportFragmentManager)
        viewPager = findViewById(R.id.view_pager)
        viewPager?.adapter = pagerAdapter

        findViewById<SmoothBottomBar>(R.id.bottom_bar).onItemSelected =
            { i: Int -> viewPager?.currentItem = i }

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                findViewById<SmoothBottomBar>(R.id.bottom_bar).itemActiveIndex = position
            }
        })

        firebaseUser = FirebaseAuth.getInstance().currentUser
    }

    override fun onResume() {
        super.onResume()
        if (firebaseUser == null) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                Log.d("App onResume", "Terdeteksi login: " + currentUser.email)
                firebaseUser = currentUser
                /*viewPager!!.adapter = null
                val pagerAdapter = MainPagerAdapter(LEVEL_USER, supportFragmentManager)
                viewPager!!.adapter = pagerAdapter
                pagerAdapter.notifyDataSetChanged()*/
            }
        }
    }
}