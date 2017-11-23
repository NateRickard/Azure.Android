package com.microsoft.azureandroid.data.services

import com.microsoft.azureandroid.data.model.*
import okhttp3.Request
import okhttp3.Response

/**
* Created by Nate Rickard on 11/7/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class ResourceListResponse<T: Resource>(
        // The URL request sent to the server.
        val request: Request? = null,
        // The server's response to the URL request.
        val response: Response? = null,
        // The json data returned by the server.
        val jsonData: String? = null,
        // The result of response deserialization.
        val result: ListResult<T>? = null
) {

    constructor(error: DataError) : this(result = ListResult(error = error))

    // Returns the associated value of the result if it is a success, null otherwise.
    val resource: ResourceList<T>? get() = result?.resource

    // Returns the associated error value if the result if it is a failure, `nil` otherwise.
    val error: DataError? get() = result?.error

    // Returns `true` if the result is a success, `false` otherwise.
    val isSuccessful get() = error == null

    // Returns `true` if the result is an error, `false` otherwise.
    val isErrored get() = error != null
}