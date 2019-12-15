package com.manickchand.lmevent.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class People (

    var id:String? = null,
    var name:String = "",
    var eventId:String? = null,
    var picture:String? = null

) : Parcelable