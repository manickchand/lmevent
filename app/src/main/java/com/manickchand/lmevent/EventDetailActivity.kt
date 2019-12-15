package com.manickchand.lmevent

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.manickchand.lmevent.interfaces.IserviceRetrofit
import com.manickchand.lmevent.model.Event
import com.manickchand.lmevent.util.KEY_EVENT
import com.manickchand.lmevent.util.RetrofitInit
import com.manickchand.lmevent.util.TAG_DEBUC
import com.manickchand.lmevent.util.convertDate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.content_event_detail.*
import kotlinx.android.synthetic.main.dialog_checkin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class EventDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    var mRetrofit: Retrofit
    var mIserviceRetrofit: IserviceRetrofit
    var lat:Double=0.0
    var lng:Double=0.0

    init {
        this.mRetrofit = RetrofitInit().getClient()
        this.mIserviceRetrofit = this.mRetrofit.create(IserviceRetrofit::class.java)
    }

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

        var mEvent = intent.getParcelableExtra(KEY_EVENT) as Event
        if (mEvent != null) {
            this.initData(mEvent)
            fab.setOnClickListener { view ->
                this.shareEvent(mEvent.description!!)
            }
            btn_checkin.setOnClickListener { view ->
                this.dialogCheckin(mEvent.id!!)
            }
        }else{
            Toast.makeText(this,R.string.error_details,Toast.LENGTH_SHORT).show()
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

    fun dialogCheckin(eventId:String){

        val builder = AlertDialog.Builder(this)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        builder.setView(inflater.inflate(R.layout.dialog_checkin, null))
        val modal = builder.create()

        modal.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.confirm), DialogInterface.OnClickListener { dialog, id ->

            val name = modal.et_name.text.toString()
            val email = modal.et_email.text.toString()

            checkin(name,email,eventId)

        })

        modal.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), DialogInterface.OnClickListener { dialog, id ->
            modal.cancel()
        })

        modal.show()
    }

    fun checkin(name:String, email:String, eventId:String){

        if(name.isNotEmpty()
            && email.isNotEmpty()){

            var call = this.mIserviceRetrofit.getAllEvents()

            call.enqueue(object : Callback<List<Event>> {
                override fun onResponse(call: Call<List<Event>>?, response: Response<List<Event>>?) {






                }
                override fun onFailure(call: Call<List<Event>>?, t: Throwable?) {


                    Log.i(TAG_DEBUC,"[Error getAllEvents] "+t.toString())
                }
            })

        }else{
            Toast.makeText(this,R.string.name_email,Toast.LENGTH_SHORT).show()
        }
    }

}
