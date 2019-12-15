package com.manickchand.lmevent.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    val id: String? = null,
    val title: String? = "",
    val description: String? = "",
    val image: String? = "",
    val date: Long? = null,
    val price: Double? = 0.0,
    val longitude: Double? = 0.0,
    val latitude: Double? = 0.0,
    val people: List<People>?,
    val cupons: List<Cupom>?

) : Parcelable