package com.presidev.maos.mitramanagement.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book(
    var bookId: String? = "",
    var mitraId: String? = "",
    var title: String? = "",
    var ketersediaan: Boolean? = true,
    var photo: String? = "",
    var penulis: String? = "",
    var description: String? = "",
    var dateCreated: String? = "",
    var waCount: Int? = 0
) : Parcelable