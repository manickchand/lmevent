package com.manickchand.lmevent.model

import java.io.Serializable

class People : Serializable {

    var id:String?
    var name:String?
    var eventId:String?
    var picture:String?

    init {
        this.id=null
        this.name=null
        this.eventId=null
        this.picture=null
    }
}