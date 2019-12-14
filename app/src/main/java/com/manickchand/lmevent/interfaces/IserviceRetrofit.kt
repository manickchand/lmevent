package com.manickchand.lmevent.interfaces

import com.manickchand.lmevent.model.Event
import retrofit2.Call
import retrofit2.http.*

interface IserviceRetrofit {

    @GET("events")
    fun getAllEvents(): Call<List<Event>>

    @GET("events/{id}")
    fun getEventById(@Path("id") id:Int): Call<Event>

    @Headers( "content-type: application/json;" )
    @POST("authentication/register")
    fun eventCheckin(@Query("eventId") eventId:String,
                 @Query("name") name:String,
                 @Query("email") email:String):Call<Boolean>
}