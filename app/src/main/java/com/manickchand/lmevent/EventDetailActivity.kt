package com.manickchand.lmevent

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.manickchand.lmevent.model.Event
import com.manickchand.lmevent.util.KEY_EVENT
import com.manickchand.lmevent.util.convertDate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.content_event_detail.*


class EventDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    var lat:Double=0.0
    var lng:Double=0.0

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
                this.shareEvent(mEvent.description!!)
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
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
        this.loadMap(mEvent.latitude!!,mEvent.longitude!!)
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

    fun loadMap(lat:Double,lng:Double){
        this.lat = lat
        this.lng = lng
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        map!!.addMarker(
            MarkerOptions().position(
                LatLng(
                   this.lat,
                    this.lng
                )
            ).title(getString(R.string.event_location))
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(this.lat,this.lng), 14f))
    }

    fun shareEvent(description:String){

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, description)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}
