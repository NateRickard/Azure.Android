package com.microsoft.azureandroid.data.constants

/**
* Created by Nate Rickard on 11/2/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

sealed class ApiValues {

    enum class HttpMethod {
        GET,
        HEAD,
        POST,
        PUT,
        DELETE,
    }

    // https://docs.microsoft.com/en-us/rest/api/documentdb/common-documentdb-rest-request-headers
    enum class HttpRequestHeader(val value: String) {
        AUTHORIZATION("Authorization"),
        CONTENTTYPE("Content-Type"),
        IFMATCH("If-Match"),
        IFNONEMATCH("If-None-Match"),
        IFMODIFIEDSINCE("If-Modified-Since"),
        USERAGENT("User-Agent"),
        XMSACTIVITYID("x-ms-activity-id"),
        XMSCONSISTENCYLEVEL("x-ms-consistency-level"),
        XMSCONTINUATION("x-ms-continuation"),
        XMSDATE("x-ms-date"),
        XMSMAXITEMCOUNT("x-ms-max-item-count"),
        XMSDOCUMENTDBPARTITIONKEY("x-ms-documentdb-partitionkey"),
        XMSDOCUMENTDBISQUERY("x-ms-documentdb-isquery"),
        XMSSESSIONTOKEN("x-ms-session-token"),
        XMSVERSION("x-ms-version"),
        AIM("A-IM"),
        XMSDOCUMENTDBPARTITIONKEYRANGEID("x-ms-documentdb-partitionkeyrangeid"),
        ACCEPTENCODING("Accept-Encoding"),
        ACCEPTLANGUAGE("Accept-Language"),
        SLUG("Slug");

        fun isRequired() : Boolean {
            return when (this) {
                AUTHORIZATION -> true
                CONTENTTYPE -> true
                XMSDATE -> true
                XMSSESSIONTOKEN -> true
                XMSVERSION -> true
                else -> false
            }
        }
    }

    enum class HttpRequestHeaderValue(val value: String) {

        // https://docs.microsoft.com/en-us/rest/api/documentdb/#supported-rest-api-versions
        API_VERSION("2017-02-22"),

        // Accept-Encoding HTTP Header; see https://tools.ietf.org/html/rfc7230#section-4.2.3
        ACCEPT_ENCODING("gzip;q=1.0, compress;q=0.5"),

        ACCEPT_JSON("${MediaTypes.JSON}; charset=utf-8")
    }

    enum class MediaTypes(val value: String) {

        JSON("application/json"),
        QUERY_JSON("application/query+json")
    }


    // https://docs.microsoft.com/en-us/rest/api/documentdb/http-status-codes-for-documentdb
    enum class StatusCode(val code: Int) {
        OK(200),
        CREATED(201),
        NO_CONTENT(204),
        BAD_REQUEST(400),
        UNAUTHORIZED(401),
        FORBIDDEN(403),
        NOT_FOUND(404),
        REQUEST_TIMEOUT(408),
        CONFLICT(409),
        PRECONDITION_FAILURE(412),
        ENTITY_TOO_LARGE(413),
        TOO_MANY_REQUESTS(429),
        RETRY_WITH(449),
        INTERNAL_SERVER_ERROR(500),
        SERVICE_UNAVAILABLE(503)
    }
}