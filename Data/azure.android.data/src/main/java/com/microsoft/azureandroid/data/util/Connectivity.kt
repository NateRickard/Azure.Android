//package com.microsoft.azureandroid.data.util
//
//import android.content.Context
//import android.net.ConnectivityManager
//import com.microsoft.azureandroid.data.AzureData
//
///**
// * Created by nater on 10/31/17.
// */
//
//class Connectivity protected constructor() {
//
//    private var _isConnected: Boolean? = null
//
//    val isNetworkAvailable: Boolean
//        get() {
//            val connectivityManager = AzureData.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            val activeNetworkInfo = connectivityManager.activeNetworkInfo
//            return activeNetworkInfo != null && activeNetworkInfo.isConnected
//        }
//
//    var isConnected: Boolean
//        get() = _isConnected!!
//        set(isConnected) {
//            _isConnected = isConnected
//        }
//
//    companion object {
//        val instance: Connectivity by lazy {
//            Connectivity()
//        }
//    }
//}
