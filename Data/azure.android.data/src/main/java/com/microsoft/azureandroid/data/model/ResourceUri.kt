package com.microsoft.azureandroid.data.model

import okhttp3.HttpUrl

/**
 * Created by nater on 10/31/17.
 */

data class UrlLink(val url: HttpUrl, val link: String)

// https://docs.microsoft.com/en-us/rest/api/documentdb/documentdb-resource-uri-syntax-for-rest
class ResourceUri(databaseName: String) {

    private val host: String = "$databaseName.documents.azure.com"

    fun database(resourceId: String? = null) : UrlLink {
        val baseLink = ""
        val itemLink = getItemLink(ResourceType.DATABASE, baseLink, resourceId)

        return getUrlLink(baseLink, itemLink, resourceId)
    }

    private fun getItemLink(resourceType: ResourceType, baseLink: String, resourceId: String?) : String {

        var fragment = ""

        resourceId?.let {
            fragment = "/$resourceId"
        }

        return when (resourceType) {
            ResourceType.DATABASE           -> "dbs$fragment"
            ResourceType.USER               -> "$baseLink/users$fragment"
            ResourceType.PERMISSION         -> "$baseLink/permissions$fragment"
            ResourceType.COLLECTION         -> "$baseLink/colls$fragment"
            ResourceType.STORED_PROCEDURE   -> "$baseLink/sprocs$fragment"
            ResourceType.TRIGGER            -> "$baseLink/triggers$fragment"
            ResourceType.UDF                -> "$baseLink/udfs$fragment"
            ResourceType.DOCUMENT           -> "$baseLink/docs$fragment"
            ResourceType.ATTACHMENT         -> "$baseLink/attachments$fragment"
            ResourceType.OFFER              -> "offers$fragment"
        }
    }

    private fun getUrlLink(baseLink: String, itemLink: String, resourceId: String?) : UrlLink {
        val url = HttpUrl.Builder()
                .scheme("https")
                .host(host)
                .addPathSegment(itemLink)
                .build()

        return UrlLink(url, if (resourceId != null) itemLink else baseLink)
    }

    private fun getUrlLinkForSelf(baseLink: String, itemLink: String, resourceId: String?) : UrlLink {
        val url = HttpUrl.Builder()
                .scheme("https")
                .host(host)
                .addPathSegment(itemLink)
                .build()

        return UrlLink(url, resourceId?.toLowerCase() ?: baseLink.split("/").last().toLowerCase())
    }
}