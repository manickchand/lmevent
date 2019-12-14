package com.manickchand.lmevent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
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

        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_events.setHasFixedSize(true)
        rv_events.layoutManager = llm

        this.requestEvents()
        swiperefresh.setColorSchemeResources(R.color.colorAccent)
        swiperefresh.setOnRefreshListener{
            this.requestEvents()
        }
    }

    fun requestEvents(){
        swiperefresh.isRefreshing = true

        var call = this.mIserviceRetrofit.getAllEvents()

        call.enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>?, response: Response<List<Event>>?) {

                mList = response!!.body()!!
                setAdapter()

                swiperefresh.isRefreshing = false
            }
            override fun onFailure(call: Call<List<Event>>?, t: Throwable?) {
                swiperefresh.isRefreshing = false
                Snackbar.make(containermain, R.string.error_get_all_events, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                Log.i(TAG_DEBUC,"[Error getAllEvents] "+t.toString())
            }
        })
    }

    fun setAdapter(){
        var adapter = EventsAdapter(this,mList)
        adapter.setReciclerViewOnClickListenerHack(this)
        rv_events.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onClickListener(v: View?, position: Int) {
        val intent = Intent(this, EventDetailActivity::class.java)
        intent.putExtra(KEY_EVENT, this.mList[position])
        startActivity(intent)
    }
}
