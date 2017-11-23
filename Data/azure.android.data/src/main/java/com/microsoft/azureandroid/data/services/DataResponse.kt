package com.microsoft.azureandroid.data.services

import com.microsoft.azureandroid.data.model.DataError
import com.microsoft.azureandroid.data.model.DataResult
import okhttp3.Request
import okhttp3.Response

/**
 * Created by Nate Rickard on 11/22/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class DataResponse (
        // The URL request sent to the server.
        val request: Request? = null,
        // The server's response to the URL request.
        val response: Response? = null,
        // The data returned by the server.
        val jsonData: String? = null,
        // The result of response deserialization.
        val result: DataResult
) {

    constructor(error: DataError) : this(result = DataResult(error = error))

    // Returns the associated error value if the result if it is a failure, `nil` otherwise.
    val error: DataError? get() = result.error

    // Returns `true` if the result is a success, `false` otherwise.
    val isSuccessful get() = error == null

    // Returns `true` if the result is an error, `false` otherwise.
    val isErrored get() = error != null
}