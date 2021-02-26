package com.presidev.maos.catatanku.reminder.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.presidev.maos.R
import com.presidev.maos.auth.preference.AuthPreference
import com.presidev.maos.catatanku.UserPreference
import com.presidev.maos.catatanku.helper.ReturnReminder
import kotlinx.android.synthetic.main.fragment_reminder.*
import kotlinx.android.synthetic.main.fragment_reminder.view.*


class ReminderFragment : Fragment(){
    private val TAG = javaClass.simpleName

    private lateinit var  reminderViewModel : ReminderViewModel

    var pref : AuthPreference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userPreference = UserPreference(context)
        pref = AuthPreference(context)
        reminderViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ReminderViewModel::class.java)
        reminderViewModel.getResultReminder().observe(viewLifecycleOwner, { result ->
            Log.d("ReminderFragment", "onViewCreated: $result")
            rv_reminder.layoutManager = LinearLayoutManager(context)
            val adapter = ReminderAdapter(result)
            rv_reminder.adapter = adapter

            if (!userPreference.hasSetReturnRelogin) {
                val returnReminder = ReturnReminder()
                for (reminder in result) {
                    if (reminder.isKembali.equals("false")) {
                        returnReminder.setReminder(context, reminder)
                    }
                }
                userPreference.hasSetReturnRelogin = true
                Log.d(TAG, "Set reminder after relogin")
            }
        })

        view.fab_add_reminder.setOnClickListener {
            val intent = Intent(context, AddEditReminderActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        reminderViewModel.loadResultReminder(pref?.id!!)
    }

    override fun onResume() {
        super.onResume()
        reminderViewModel.loadResultReminder(pref?.id!!)
    }

}