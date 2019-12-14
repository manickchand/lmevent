package com.manickchand.lmevent.model

import java.io.Serializable

class Cupom : Serializable {

    var id:String?
    var discount:Int?
    var eventId:String?

    init {
        this.id=null
        this.discount=null
        this.eventId=null
    }
}