package com.presidev.maos

import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.github.naz013.smoothbottombar.SmoothBottomBar
import com.github.naz013.smoothbottombar.Tab
import com.presidev.maos.bookmark.view.BookmarkFragment
import com.presidev.maos.dashboard.view.DashboardFragment
import com.presidev.maos.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_app.*

class App : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        loadFragment(DashboardFragment())
        findViewById<SmoothBottomBar>(R.id.bottomBar2).setTabs(createTabs4())

        findViewById<SmoothBottomBar>(R.id.bottomBar2).setOnTabSelectedListener { showTab(it) }
    }

    private fun showTab(it: Int) {
        when(it) {
            0 -> loadFragment(DashboardFragment())
            1 -> loadFragment(BookmarkFragment())
            2 -> loadFragment(ProfileFragment())
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