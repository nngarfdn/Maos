package com.presidev.maos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.naz013.smoothbottombar.SmoothBottomBar
import com.github.naz013.smoothbottombar.Tab
import com.presidev.maos.bookmark.view.BookmarkFragment
import com.presidev.maos.dashboard.view.DashboardFragment
import com.presidev.maos.login.preference.AccountPreference
import com.presidev.maos.login.view.LoginActivity
import com.presidev.maos.profile.mitra.MitraProfileFragment
import com.presidev.maos.profile.user.UserProfileFragment
import com.presidev.maos.utils.AppUtils.hideStatusBar
import com.presidev.maos.utils.Constants.LEVEL_MITRA
import com.presidev.maos.utils.Constants.LEVEL_USER

class App : AppCompatActivity() {

    private var accountPreference: AccountPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar(this, supportActionBar)
        setContentView(R.layout.activity_app)

        accountPreference = AccountPreference(this)

        loadFragment(DashboardFragment())
        findViewById<SmoothBottomBar>(R.id.bottomBar2).setTabs(createTabs4())
        findViewById<SmoothBottomBar>(R.id.bottomBar2).setOnTabSelectedListener { showTab(it) }
    }

    private fun showTab(it: Int) {
        when(it) {
            0 -> loadFragment(DashboardFragment())
            1 -> loadFragment(BookmarkFragment())
            2 -> {
                if (accountPreference!!.level == null) {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                else if (accountPreference!!.level.equals(LEVEL_MITRA)) loadFragment(MitraProfileFragment())
                else if (accountPreference!!.level.equals(LEVEL_USER)) loadFragment(UserProfileFragment())
            }
        }
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .addToBackStack(null)
                    .commit()
            return true
        }
        return false
    }

    private fun createTabs4(): List<Tab> {
        return listOf(
                Tab(icon = R.drawable.ic_baseline_home_24, title = "Home"),
                Tab(icon = R.drawable.ic_baseline_bookmark_24, title = "Bookmark"),
                Tab(icon = R.drawable.ic_baseline_person_24, title = "Profile")
        )
    }



}