package com.manickchand.lmevent

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.manickchand.lmevent.model.Event
import com.manickchand.lmevent.util.KEY_EVENT
import com.manickchand.lmevent.util.convertDate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.content_event_detail.*

class EventDetailActivity : AppCompatActivity() {

//    var mEvent:Event?
//
//    init {
//        this.mEvent = null
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                finish()
            }
        })

        var mEvent = intent.getSerializableExtra(KEY_EVENT) as Event
        if (mEvent != null) {
            this.initData(mEvent)
            fab.setOnClickListener { view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }else{
            Toast.makeText(this,"Erro ao carregar detalhes",Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initData(mEvent:Event){
        setTitle("")
        tv_title_detail.text = mEvent.title
        tv_date_detail.text = convertDate(mEvent.date!!)
        tv_description_detail.text = mEvent.description
        this.getImage(mEvent.image!!)

    }


    fun getImage(urlImage:String){
        try {
            Picasso.get().load(urlImage)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(iv_event_detail)
        }catch (e:Exception){
            e.stackTrace
        }

    }
}
