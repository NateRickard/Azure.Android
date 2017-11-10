package com.microsoft.azureandroid.data.model.contract

import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.HttpException

/**
 * Created by nater on 10/31/17.
 */

open class BaseContract {
    @SerializedName("_rid")
    var rid: String? = null

    @SerializedName("cod")
    private val httpCode: Int = 0

//    internal inner class Weather {
//        var description: String? = null
//    }

    /**
     * The web service always returns a HTTP header code of 200 and communicates errors
     * through a 'cod' field in the JSON payload of the response body.
     */
    fun filterWebServiceErrors(): Observable<BaseContract> {
        return if (httpCode == 200) {
            Observable.just(this)
        } else {
            Observable.error(Exception("There was a problem fetching the data."))
        }
    }
}