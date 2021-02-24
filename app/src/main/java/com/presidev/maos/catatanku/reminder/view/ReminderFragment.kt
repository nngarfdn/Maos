package com.presidev.maos.catatanku.reminder.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.presidev.maos.R
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_reminder.*
import kotlinx.android.synthetic.main.fragment_reminder.view.*


class ReminderFragment : Fragment(){

    private lateinit var  reminderViewModel : ReminderViewModel


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

        reminderViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ReminderViewModel::class.java)
        reminderViewModel.getResultReminder().observe(viewLifecycleOwner, { result ->
            Log.d("ReminderFragment", "onViewCreated: $result")
            rv_reminder.layoutManager = LinearLayoutManager(context)
            val adapter = ReminderAdapter(result)
            rv_reminder.adapter = adapter
        })

        view.fab_add_reminder.setOnClickListener {
            val intent = Intent(context, AddEditReminderActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        reminderViewModel.loadResultReminder()
    }

    override fun onResume() {
        super.onResume()
        reminderViewModel.loadResultReminder()
    }

}