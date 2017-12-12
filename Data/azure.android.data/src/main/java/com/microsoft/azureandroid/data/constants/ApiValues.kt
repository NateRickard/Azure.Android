package com.microsoft.azureandroid.data.constants

/**
* Created by Nate Rickard on 11/2/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

sealed class ApiValues {

    enum class HttpMethod {
        Get,
        Head,
        Post,
        Put,
        Delete
    }

    // https://docs.microsoft.com/en-us/rest/api/documentdb/common-documentdb-rest-request-headers
    enum class HttpRequestHeader(val value: String) {
        Authorization("Authorization"),
        ContentType("Content-Type"),
        IfMatch("If-Match"),
        IfNoneMatch("If-None-Match"),
        IfModifiedSince("If-Modified-Since"),
        UserAgent("User-Agent"),
        XMSActivityId("x-ms-activity-id"),
        XMSConsistencyLevel("x-ms-consistency-level"),
        XMSContinuation("x-ms-continuation"),
        XMSDate("x-ms-date"),
        XMSMaxItemCount("x-ms-max-item-count"),
        XMSDocumentDBPartitionKey("x-ms-documentdb-partitionkey"),
        XMSDocumentDBIsQuery("x-ms-documentdb-isquery"),
        XMSSessionToken("x-ms-session-token"),
        XMSVersion("x-ms-version"),
        AIM("A-IM"),
        XMSDocumentDBPartitionKeyRangeId("x-ms-documentdb-partitionkeyrangeid"),
        AcceptEncoding("Accept-Encoding"),
        AcceptLanguage("Accept-Language"),
        Slug("Slug");

        fun isRequired() : Boolean = when (this) {
            Authorization -> true
            ContentType -> true
            XMSDate -> true
            XMSSessionToken -> true
            XMSVersion -> true
            else -> false
        }
    }

    enum class HttpRequestHeaderValue(val value: String) {

        // https://docs.microsoft.com/en-us/rest/api/documentdb/#supported-rest-api-versions
        ApiVersion("2017-02-22"),

        // Accept-Encoding HTTP Header; see https://tools.ietf.org/html/rfc7230#section-4.2.3
        AcceptEncoding("gzip;q=1.0, compress;q=0.5")
    }

    enum class MediaTypes(val value: String) {

        Json("application/json"),
        QueryJson("application/query+json")
    }


    // https://docs.microsoft.com/en-us/rest/api/documentdb/http-status-codes-for-documentdb
    enum class StatusCode(val code: Int) {
        OK(200),
        CREATED(201),
        NO_CONTENT(204),
        NotModified(304), //not documented, but 304 can be returned when specifying IfNoneMatch header with etag value
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