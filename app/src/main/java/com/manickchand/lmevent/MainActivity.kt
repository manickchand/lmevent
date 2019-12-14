package com.manickchand.lmevent

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.manickchand.lmevent.interfaces.IserviceRetrofit
import com.manickchand.lmevent.model.Event
import com.manickchand.lmevent.util.RetrofitInit
import com.manickchand.lmevent.util.TAG_DEBUC
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    var mRetrofit:Retrofit
    var mIserviceRetrofit: IserviceRetrofit

    init {
        this.mRetrofit = RetrofitInit().getClient()
        this.mIserviceRetrofit = this.mRetrofit.create(IserviceRetrofit::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var call = this.mIserviceRetrofit.getAllEvents()

//        try {
           call.enqueue(object : Callback<List<Event>> {
               override fun onResponse(call: Call<List<Event>>?, response: Response<List<Event>>?) {

                   for(res in response!!.body()!!){
                       Log.i(TAG_DEBUC,"response: "+res.title)
                   }
               }
               override fun onFailure(call: Call<List<Event>>?, t: Throwable?) {
                   Log.i(TAG_DEBUC,"Erro ao pegar url "+t.toString())
               }
           })
//        }catch (ex:Exception) {
//            Log.i(TAG_DEBUC,"error: "+ex.message)
//            ex.printStackTrace()
//        }
    }
}
