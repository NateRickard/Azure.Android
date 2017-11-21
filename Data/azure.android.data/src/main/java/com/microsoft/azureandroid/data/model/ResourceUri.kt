package com.microsoft.azureandroid.data.model

import android.net.Uri
import okhttp3.HttpUrl

/**
* Created by Nate Rickard on 10/31/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

// https://docs.microsoft.com/en-us/rest/api/documentdb/documentdb-resource-uri-syntax-for-rest
class ResourceUri(databaseName: String) {

    private val host: String = "$databaseName.documents.azure.com"

    fun forDatabase(databaseId: String? = null) : UrlLink {

        val baseLink = ""
        val itemLink = getItemLink(ResourceType.DATABASE, baseLink, databaseId)

        return getUrlLink(baseLink, itemLink, databaseId)
    }

    fun forUser(databaseId: String, userId: String? = null) : UrlLink {

        val baseLink = "dbs/$databaseId"
        val itemLink = getItemLink(ResourceType.USER, baseLink, userId)

        return getUrlLink(baseLink, itemLink, userId)
    }

    fun forPermission(databaseId: String, userId: String, permissionId: String?): UrlLink {

        val baseLink = "dbs/$databaseId/users/$userId"
        val itemLink = getItemLink(ResourceType.PERMISSION, baseLink, permissionId)

        return getUrlLink(baseLink, itemLink, permissionId)
    }

    fun forPermission(baseLink: String, resourceId: String?): UrlLink {

        val itemLink = getItemLink(ResourceType.PERMISSION, baseLink, resourceId)

        return getUrlLinkForSelf(baseLink, itemLink, resourceId)
    }

    fun forCollection(databaseId: String, collectionId: String? = null) : UrlLink {

        val baseLink = "dbs/$databaseId"
        val itemLink = getItemLink(ResourceType.COLLECTION, baseLink, collectionId)

        return getUrlLink(baseLink, itemLink, collectionId)
    }

    fun forDocument(databaseId: String, collectionId: String, documentId: String? = null) : UrlLink {

        val baseLink = "dbs/$databaseId/colls/$collectionId"
        val itemLink = getItemLink(ResourceType.DOCUMENT, baseLink, documentId)

        return getUrlLink(baseLink, itemLink, documentId)
    }

    fun forDocument(baseLink: String, documentId: String? = null) : UrlLink {

        val itemLink = getItemLink(ResourceType.DOCUMENT, baseLink, documentId)

        return getUrlLinkForSelf(baseLink, itemLink, documentId)
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
            fragment = "$resourceId"
        }

        val builder = Uri.Builder()
                .appendEncodedPath(baseLink.trim('/'))
                .appendEncodedPath(resourceType.path)
                .appendEncodedPath(fragment)

//        when (resourceType) {
//            ResourceType.DATABASE           -> builder.appendPath("dbs")
//            ResourceType.USER               -> builder.appendPath("users") // "$baseLink/users$fragment"
//            ResourceType.PERMISSION         -> builder.appendPath("permissions") // "$baseLink/permissions$fragment"
//            ResourceType.COLLECTION         -> "$baseLink/colls$fragment"
//            ResourceType.STORED_PROCEDURE   -> "$baseLink/sprocs$fragment"
//            ResourceType.TRIGGER            -> "$baseLink/triggers$fragment"
//            ResourceType.UDF                -> "$baseLink/udfs$fragment"
//            ResourceType.DOCUMENT           -> "$baseLink/docs$fragment"
//            ResourceType.ATTACHMENT         -> "$baseLink/attachments$fragment"
//            ResourceType.OFFER              -> "offers$fragment"
//        }

        return builder.build().path.trim('/')
    }

    private fun getUrlLink(baseLink: String, itemLink: String, resourceId: String? = null) : UrlLink {
        val url = HttpUrl.Builder()
                .scheme("https")
                .host(host)
                .addPathSegment(itemLink.trimStart('/'))
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