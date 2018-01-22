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
        request: Request? = null,
        // The server's response to the URL request.
        response: Response? = null,
        // The json data returned by the server.
        jsonData: String? = null,
        // The result of response deserialization.
        override val result: Result<ResourceList<T>>) : ResourceResponse<ResourceList<T>>(request, response, jsonData, result) {

    constructor(
            // the error
            error: DataError,
            // The URL request sent to the server.
            request: Request? = null,
            // The server's response to the URL request.
            response: Response? = null,
            // The json data returned by the server.
            jsonData: String? = null) : this(request, response, jsonData, Result(error))
}