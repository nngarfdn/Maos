package com.presidev.maos.mitra.model

data class Book(
        var bookId: String? = "",
        var mitraId: String? = "",
        var title : String? = "",
        var ketersediaan : Boolean? = true,
        var photo : String? = "",
        var description : String? = "",
        var sinopsis : String? = ""
)