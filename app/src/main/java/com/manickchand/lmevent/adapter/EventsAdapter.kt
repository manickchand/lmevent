package com.manickchand.lmevent.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.manickchand.lmevent.R
import com.manickchand.lmevent.interfaces.RecyclerViewOnClickListenerHack
import com.manickchand.lmevent.model.Event
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row.view.*

class EventsAdapter(context: Context,
                    list: List<Event>) : RecyclerView.Adapter<EventsAdapter.MyViewHolder?>() {

    var mContext =context
    var mList = list
    var mlayoutInflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater //
    var mReciclerViewOnClickListenerHack: RecyclerViewOnClickListenerHack? = null // interface de click
    lateinit var view: View

    //infla view da linha
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        view = mlayoutInflater.inflate(R.layout.item_row,parent,false)
        return MyViewHolder(view)
    }

    // seta dados de cada linha
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //tenta carregar img
        try {
            Picasso.get().load(mList.get(position).image)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(holder.ivEvent)
        }catch (e:Exception){
            e.stackTrace
        }
        holder.tvTitle.text = mList.get(position).title
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setReciclerViewOnClickListenerHack(r: RecyclerViewOnClickListenerHack) {
        this.mReciclerViewOnClickListenerHack = r
    }

    inner class MyViewHolder(itemView:View): RecyclerView.ViewHolder(itemView),View.OnClickListener{

        val ivEvent: ImageView
        var tvTitle: TextView

        init {
            itemView.setOnClickListener(this)
            ivEvent = itemView.iv_event
            tvTitle = itemView.tv_title
        }

        // clickListener de cada posicao do adapter
        override fun onClick(v: View?) {
            if (mReciclerViewOnClickListenerHack != null) {
                mReciclerViewOnClickListenerHack!!.onClickListener(v!!, adapterPosition)
            }
        }
    }
}