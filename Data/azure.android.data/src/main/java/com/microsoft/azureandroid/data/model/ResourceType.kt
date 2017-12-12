package com.microsoft.azureandroid.data.model

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
* Created by Nate Rickard on 10/31/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

enum class ResourceType(val path: String, fullname: String, val type: Type) {

    Database("dbs", "Database", object : TypeToken<com.microsoft.azureandroid.data.model.Database>() {}.type),
    User("users", "User", object : TypeToken<com.microsoft.azureandroid.data.model.User>() {}.type),
    Permission("permissions", "Permission", object : TypeToken<com.microsoft.azureandroid.data.model.Permission>() {}.type),
    Collection("colls", "DocumentCollection", object : TypeToken<DocumentCollection>() {}.type),
    StoredProcedure("sprocs", "StoredProcedure", object : TypeToken<com.microsoft.azureandroid.data.model.StoredProcedure>() {}.type),
    Trigger("triggers", "Trigger", object : TypeToken<com.microsoft.azureandroid.data.model.Trigger>() {}.type),
    Udf("udfs", "UserDefinedFunction", object : TypeToken<UserDefinedFunction>() {}.type),
    Document("docs", "Document", object : TypeToken<com.microsoft.azureandroid.data.model.Document>() {}.type),
    Attachment("attachments", "Attachment", object : TypeToken<com.microsoft.azureandroid.data.model.Attachment>() {}.type),
    Offer("offers", "Offer", object : TypeToken<com.microsoft.azureandroid.data.model.Offer>() {}.type);

    val listName: String = "${fullname}s"

    companion object {

        fun<T: Resource> fromType(clazz: Class<T>) : ResourceType {

            //is this a Document?
            if (Document::class.java.isAssignableFrom(clazz)) {
                return Document
            }

            return ResourceType.values().find {
                it.type == clazz
            } ?: throw Exception("Unable to determine resource type requested")
        }
    }
}