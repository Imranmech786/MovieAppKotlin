package com.imran.myapplication.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class Network {

    companion object {
        private fun getNetworkInfo(context: Context): NetworkInfo? {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo
        }

        fun isConnected(context: Context?): Boolean {
            if (context == null) return true
            val networkInfo = getNetworkInfo(context)
            return networkInfo != null && networkInfo.isConnected
        }
    }
}