package com.microsoft.azureandroid.data.model

import okhttp3.HttpUrl

/**
* Created by Nate Rickard on 10/31/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

// https://docs.microsoft.com/en-us/rest/api/documentdb/documentdb-resource-uri-syntax-for-rest
class ResourceUri(databaseName: String) {

    private val host: String = "$databaseName.documents.azure.com"

    fun forDatabase(resourceId: String? = null) : UrlLink {

        val baseLink = ""
        val itemLink = getItemLink(ResourceType.DATABASE, baseLink, resourceId)

        return getUrlLink(baseLink, itemLink, resourceId)
    }

    fun forCollection(databaseId: String, resourceId: String? = null) : UrlLink {

        val baseLink = "dbs/$databaseId"
        val itemLink = getItemLink(ResourceType.COLLECTION, baseLink, resourceId)

        return getUrlLink(baseLink, itemLink, resourceId)
    }

    fun forDocument(databaseId: String, collectionId: String, resourceId: String? = null) : UrlLink {

        val baseLink = "dbs/$databaseId/colls/$collectionId"
        val itemLink = getItemLink(ResourceType.DOCUMENT, baseLink, resourceId)

        return getUrlLink(baseLink, itemLink, resourceId)
    }

    fun forDocument(baseLink: String, resourceId: String? = null) : UrlLink {

        val itemLink = getItemLink(ResourceType.DOCUMENT, baseLink, resourceId)

        return getUrlLinkForSelf(baseLink, itemLink, resourceId)
    }

    fun forOffer() : UrlLink {

        val baseLink = ""
        val itemLink = getItemLink(ResourceType.OFFER, baseLink)

        return getUrlLink(baseLink, itemLink)
    }

    fun forOffer(resourceId: String? = null) : UrlLink {

        val baseLink = ""
        val itemLink = getItemLink(ResourceType.OFFER, baseLink, resourceId)

        return getUrlLinkForSelf(baseLink, itemLink, resourceId)
    }

    private fun getItemLink(resourceType: ResourceType, baseLink: String, resourceId: String? = null) : String {

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

    private fun getUrlLink(baseLink: String, itemLink: String, resourceId: String? = null) : UrlLink {
        val url = HttpUrl.Builder()
                .scheme("https")
                .host(host)
                .addPathSegment(itemLink)
                .build()

        return UrlLink(url, if (resourceId != null) itemLink else baseLink)
    }

    private fun getUrlLinkForSelf(baseLink: String, itemLink: String, resourceId: String? = null) : UrlLink {
        val url = HttpUrl.Builder()
                .scheme("https")
                .host(host)
                .addPathSegment(itemLink)
                .build()

        return UrlLink(url, resourceId?.toLowerCase() ?: baseLink.split("/").last().toLowerCase())
    }
}