package com.microsoft.azureandroid.data.services

import android.os.AsyncTask
import java.io.IOException

import okhttp3.*

/**
 * Created by nater on 10/31/17.
 */

class WebServiceTask internal constructor(private val callback: (Response) -> Unit) : AsyncTask<Request, Void, Response>() {    //, private val _method: ApiValues.HttpMethod, private val headers: Headers, private val _parameters: HashMap<String, Any>?,
//                                          private val _authString: String, private val _date: String, private val _query: String?) : AsyncTask<Request, Void, String>() {

//    private var _body: RequestBody? = null

    init {
//        _delegate = delegate
//        _body = RequestBody.create(JSON, "")
    }

    override fun doInBackground(vararg request: Request): Response {



//            var request: Request
//            val response: ResourceResponse

//        val paramsJson: JSONObject

//            if (_parameters != null) {
//                try {
//                    paramsJson = JSONObject(_parameters)
//                    _body = RequestBody.create(JSON, paramsJson.toString())
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//            }

//            when (_method) {
//
//                ApiValues.HttpMethod.GET -> request = Request.Builder()
//                        .headers(headers)
////                        .addHeader("x-ms-date", _date)
////                        .addHeader("x-ms-version", "2015-08-06")
////                        .addHeader("authorization", _authString)
////                        .addHeader("Accept", "application/json")
//                        .url(urls[0])
//                        .get()
//                        .build()
//                ApiValues.HttpMethod.POST -> {
//                    request = Request.Builder()
//                            .headers(headers)
////                            .addHeader("x-ms-date", _date)
////                            .addHeader("x-ms-version", "2015-08-06")
////                            .addHeader("authorization", _authString)
////                            .addHeader("Accept", "application/json")
//                            .url(urls[0])
//                            .post(_body)
//                            .build()
//
//                    if (_query != null && !_query.isEmpty()) {
//                        request = Request.Builder()
//                                .headers(headers)
////                                .addHeader("x-ms-date", _date)
////                                .addHeader("x-ms-version", "2015-08-06")
//                                .addHeader("x-ms-documentdb-isquery", _query)
////                                .addHeader("authorization", _authString)
////                                .addHeader("Accept", "application/json")
//                                .url(urls[0])
//                                .post(_body)
//                                .build()
//                    }
//                }
//                else -> request = Request.Builder()
//                        .headers(headers)
//                        .url(urls[0])
//                        .build()
//            }


        try {
            // we use the OkHttp library from https://github.com/square/okhttp
            val client = OkHttpClient()

            return client.newCall(request[0]).execute()

//            if (response.isSuccessful) {
//                return response.body()!!.string()
//            } else {
//                //Logger.getInstance().Log("response error: " + response.body().string());
//            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun onPostExecute(response: Response) {

        super.onPostExecute(response)

        if (!isCancelled) {
            callback(response)
        }
    }
}