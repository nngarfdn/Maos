package com.presidev.maos.borrowbook

import com.presidev.maos.mitramanagement.model.Book

interface PeminjamanCallback {
    fun onClickPinjam(code : String)
}