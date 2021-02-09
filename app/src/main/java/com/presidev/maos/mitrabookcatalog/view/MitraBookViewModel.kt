package com.presidev.maos.mitrabookcatalog.view

import androidx.lifecycle.ViewModel
import com.presidev.maos.mitrabookcatalog.repository.MitraBookRepository

class MitraBookViewModel : ViewModel(){

    private val repository = MitraBookRepository()

    fun getResultByMitraId()  = repository.getResultsByMitraId()
    fun loadResultBymitraId(mitraId: String) = repository.getProyekByMitraID(mitraId)

    fun getResultByMitraIdPopuler()  = repository.getResultsByMitraIdPopuler()
    fun loadResultBymitraIdPopuler(mitraId: String) = repository.getProyekByMitraIDPopuler(mitraId)

}