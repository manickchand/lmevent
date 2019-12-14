package com.manickchand.lmevent.model

import java.util.*
import kotlin.collections.ArrayList

class Event {

    var id:String?
    var title:String?
    var description:String?
    var date: Long?
    var image:String?
    var latitude:Double?
    var longitude:Double?
    var price:Double?
    var cupons:List<Cupom>
    var peoples:List<People>

    init {
        this.id=null
        this.title=null
        this.description=null
        this.date=null
        this.image=null
        this.latitude=null
        this.longitude=null
        this.price=null
        this.cupons=ArrayList<Cupom>()
        this.peoples=ArrayList<People>()
    }
}