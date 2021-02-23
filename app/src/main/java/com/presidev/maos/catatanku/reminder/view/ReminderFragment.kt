package com.presidev.maos.catatanku.reminder.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.presidev.maos.R
import kotlinx.android.synthetic.main.fragment_reminder.view.*


class ReminderFragment : Fragment(){


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
        view.fab_add_reminder.setOnClickListener {
            val intent = Intent(context, AddEditReminderActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        private const val TAG = "ReminderFragment"
    }

}