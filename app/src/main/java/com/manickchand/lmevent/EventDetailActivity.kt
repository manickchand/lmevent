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
import com.google.android.material.snackbar.Snackbar
import com.manickchand.lmevent.interfaces.IserviceRetrofit
import com.manickchand.lmevent.model.CheckinDTO
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

        //BOTAO DE VOLTAR NA TOOLBAR
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                finish()
            }
        })

        //RECUPERA EVENTO PASSADO DA MAIN
        var mEvent = intent.getParcelableExtra(KEY_EVENT) as Event
        if (mEvent != null) {
            this.initData(mEvent)
            fab.setOnClickListener {
                this.shareEvent(mEvent.description)
            }
            btn_checkin.setOnClickListener {
                this.dialogCheckin(mEvent.id ?: "")
            }
        }else{
            Toast.makeText(this,R.string.error_details,Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    //SETA DADOS NA VIEW
    private fun initData(mEvent:Event){
        setTitle("")
        tv_title_detail.text = mEvent.title
        tv_date_detail.text = convertDate(mEvent.date)
        tv_description_detail.text = mEvent.description
        this.getImage(mEvent.image ?: "")
        this.loadMap(mEvent.latitude,mEvent.longitude)
    }

    //CARREGA IMAGEM DO EVENTO
    private fun getImage(urlImage:String){
        try {
            Picasso.get().load(urlImage)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(iv_event_detail)
        }catch (e:Exception){
            e.stackTrace
        }
    }

    //INICIALIZA MAPA
    private fun loadMap(lat:Double?,lng:Double?){
        this.lat = lat ?: 0.0
        this.lng = lng ?: 0.0
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    //ADICIONA MARCADOR E MOVE CAMERA DO MAPA
    override fun onMapReady(map: GoogleMap?) {
        map!!.addMarker(
            MarkerOptions().position(
                LatLng(
                   this.lat,
                    this.lng
                )
            ).title(getString(R.string.event_location))
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(this.lat,this.lng), 18f))
    }

    //COMPARTILHA DESCRICAO DO EVENTO
    private fun shareEvent(description:String?){

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, description ?: "")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    //CRIA MODAL DE CHECKIN
    private fun dialogCheckin(eventId:String){

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

    //FAZ POST DE CHECKIN
    private fun checkin(name:String, email:String, eventId:String){

        if(name.isNotEmpty()
            && email.isNotEmpty()){

            val checkinDTO = CheckinDTO(eventId,name,email)

            var call = this.mIserviceRetrofit.eventCheckin(checkinDTO)

            fl_load.visibility = View.VISIBLE

            call.enqueue(object : Callback<CheckinDTO> {
                override fun onResponse(call: Call<CheckinDTO>?, response: Response<CheckinDTO>?) {

                    fl_load.visibility = View.GONE

                    Snackbar.make(containerdetail, R.string.success_checkin, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()

                }
                override fun onFailure(call: Call<CheckinDTO>?, t: Throwable?) {

                    fl_load.visibility = View.GONE

                    Snackbar.make(containerdetail, R.string.error_checkin, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()

                    Log.i(TAG_DEBUC,"[Error eventCheckin] "+t.toString())
                }
            })

        }else{
            Toast.makeText(this,R.string.name_email,Toast.LENGTH_SHORT).show()
        }
    }

}
