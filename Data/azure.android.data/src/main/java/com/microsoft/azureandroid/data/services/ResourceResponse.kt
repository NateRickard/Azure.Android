package com.microsoft.azureandroid.data.services

import com.microsoft.azureandroid.data.model.DataError
import com.microsoft.azureandroid.data.model.Resource
import com.microsoft.azureandroid.data.model.Result
import okhttp3.Request
import okhttp3.Response

/**
* Created by Nate Rickard on 11/7/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class ResourceResponse<T: Resource>(
        // The URL request sent to the server.
        var request: Request? = null,
        // The server's response to the URL request.
        var response: Response? = null,
        // The data returned by the server.
        var jsonData: String? = null,
        // The result of response deserialization.
        var result: Result<T>? = null
        ) {

    constructor(error: DataError) : this(result = Result(error = error))

    // Returns the associated value of the result if it is a success, null otherwise.
    val resource: T? get() = result?.resource

    // Returns the associated error value if the result if it is a failure, `nil` otherwise.
    val error: DataError? get() = result?.error

    // Returns `true` if the result is a success, `false` otherwise.
    val isSuccessful get() = error == null

    // Returns `true` if the result is an error, `false` otherwise.
    val isErrored get() = error != null
}