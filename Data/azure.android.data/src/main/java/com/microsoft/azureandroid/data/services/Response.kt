package com.microsoft.azureandroid.data.services

import com.microsoft.azureandroid.data.model.DataError
import com.microsoft.azureandroid.data.model.Result
import okhttp3.Request

/**
 * Created by Nate Rickard on 1/19/18.
 * Copyright © 2018 Nate Rickard. All rights reserved.
 */

open class Response(
        // The URL request sent to the server.
        val request: Request? = null,
        // The server's response to the URL request.
        val response: okhttp3.Response? = null,
        // The data returned by the server.
        val jsonData: String? = null,
        // The result of response deserialization.
        open val result: Result<*>
) {

    constructor(
            // the error
            error: DataError,
            // The URL request sent to the server.
            request: Request? = null,
            // The server's response to the URL request.
            response: okhttp3.Response? = null,
            // The json data returned by the server.
            jsonData: String? = null) : this(request, response, jsonData, Result<Unit>(error))

    /**
     * Returns the associated error value if the result if it is a failure, null otherwise.
     */
    val error: DataError? get() = result.error

    /**
     * Returns `true` if the result is a success, `false` otherwise.
     */
    val isSuccessful get() = error == null

    /**
     * Returns `true` if the result is an error, `false` otherwise.
     */
    val isErrored get() = error != null
}