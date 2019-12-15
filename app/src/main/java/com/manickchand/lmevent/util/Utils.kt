package com.manickchand.lmevent.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.text.SimpleDateFormat
import java.util.*

fun hasInternet(context: Context?): Boolean {
    val connectivityManager =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        val network = connectivityManager?.activeNetwork
        val connection = connectivityManager?.getNetworkCapabilities(network)
        return connection != null && (
                connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    } else {
        val activeNetwork = connectivityManager?.activeNetworkInfo
        if (activeNetwork != null) {
            return (activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                    activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
        }
        return false
    }
}

fun convertDate(time:Long?):String{
    if(time != null){
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(date)
    }else{
        return "01/01/1970"
    }
}