package com.microsoft.azureandroid.data.services

import com.microsoft.azureandroid.data.model.*
import okhttp3.Request
import okhttp3.Response

/**
 * Created by nater on 11/7/17.
 */

class ListResponse<T: Resource>(
        // The URL request sent to the server.
        var request: Request? = null,
        // The server's response to the URL request.
        var response: Response? = null,
        // The json data returned by the server.
        var jsonData: String? = null,
        // The result of response deserialization.
        var result: ListResult<T>? = null
) {

    constructor(error: DataError) : this(result = ListResult(error = error))

    // Returns the associated value of the result if it is a success, null otherwise.
    val resource: ResourceList<T>? get() = result?.resource

    // Returns the associated error value if the result if it is a failure, `nil` otherwise.
    val error: Error? get() = result?.error

    // Returns `true` if the result is a success, `false` otherwise.
    val isSuccessful get() = error == null

    // Returns `true` if the result is an error, `false` otherwise.
    val isErrored get() = error != null
}