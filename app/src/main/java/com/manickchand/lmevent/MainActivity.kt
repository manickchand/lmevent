package com.manickchand.lmevent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.manickchand.lmevent.adapter.EventsAdapter
import com.manickchand.lmevent.interfaces.IserviceRetrofit
import com.manickchand.lmevent.interfaces.RecyclerViewOnClickListenerHack
import com.manickchand.lmevent.model.Event
import com.manickchand.lmevent.util.KEY_EVENT
import com.manickchand.lmevent.util.RetrofitInit
import com.manickchand.lmevent.util.TAG_DEBUC
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity(),RecyclerViewOnClickListenerHack {

    var mRetrofit:Retrofit
    var mIserviceRetrofit: IserviceRetrofit
    var mList:List<Event>

    init {
        this.mRetrofit = RetrofitInit().getClient()
        this.mIserviceRetrofit = this.mRetrofit.create(IserviceRetrofit::class.java)
        this.mList = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //recyclerview
        var llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL

        rv_events.setHasFixedSize(true)
        rv_events.layoutManager = llm

        var call = this.mIserviceRetrofit.getAllEvents()

       call.enqueue(object : Callback<List<Event>> {
           override fun onResponse(call: Call<List<Event>>?, response: Response<List<Event>>?) {

               mList = response!!.body()!!
               setAdapret()

               for(res in response!!.body()!!){
                   Log.i(TAG_DEBUC,"response: "+res.image)
               }
           }
           override fun onFailure(call: Call<List<Event>>?, t: Throwable?) {
               Log.i(TAG_DEBUC,"Erro ao pegar url "+t.toString())
           }
       })

    }

    fun setAdapret(){
        var adapter = EventsAdapter(this,mList)
        adapter.setReciclerViewOnClickListenerHack(this)
        rv_events.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    override fun onClickListener(v: View?, position: Int) {
        val intent = Intent(this, EventDetailActivity::class.java)
        intent.putExtra(KEY_EVENT, this.mList.get(position))
        startActivity(intent)
//        Toast.makeText(this, "Clicado", Toast.LENGTH_SHORT).show()
    }
}
