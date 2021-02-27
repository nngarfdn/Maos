package com.presidev.maos.dashboard.view

import androidx.lifecycle.ViewModel
import com.presidev.maos.dashboard.repository.DashboardRepository

class DashboardViewModel : ViewModel() {

    val repository = DashboardRepository()

    fun getResultMitra()  = repository.getResultsMitra()
    fun loadResultMitra() = repository.getMitra()


}