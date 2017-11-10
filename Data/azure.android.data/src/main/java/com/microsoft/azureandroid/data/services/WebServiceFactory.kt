package com.microsoft.azureandroid.data.services

import retrofit2.Retrofit

/**
 * Created by nater on 10/31/17.
 */

/**
 * Creates a retrofit service from an arbitrary class (clazz)
 * @param clazz Java interface of the retrofit service
 * @return retrofit service with defined endpoint
 */
fun <T> create(clazz: Class<T>): T {
    val restAdapter = Retrofit.Builder()
            .baseUrl("")
//            .setEndpoint(DBConstants.EndpointUrl)
//            .setLogLevel(  Retrofit.LogLevel.FULL)
            .build()

    return restAdapter.create(clazz)
}