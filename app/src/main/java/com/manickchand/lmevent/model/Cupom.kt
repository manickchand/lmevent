package com.manickchand.lmevent.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cupom  (

    var id:String? = null,
    var discount:Int = 0,
    var eventId:String? = null

): Parcelable