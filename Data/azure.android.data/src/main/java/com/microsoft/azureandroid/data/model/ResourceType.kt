package com.microsoft.azureandroid.data.model

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
* Created by Nate Rickard on 10/31/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

enum class ResourceType(val path: String, val fullname: String, val type: Type) {

    DATABASE("dbs", "Database", object : TypeToken<Database>() {}.type),
    USER("users", "User", object : TypeToken<User>() {}.type),
    PERMISSION("permissions", "Permission", object : TypeToken<Permission>() {}.type),
    COLLECTION("colls", "DocumentCollection", object : TypeToken<DocumentCollection>() {}.type),
    STORED_PROCEDURE("sprocs", "StoredProcedure", object : TypeToken<StoredProcedure>() {}.type),
    TRIGGER("triggers", "Trigger", object : TypeToken<Database>() {}.type),
    UDF("udfs", "UserDefinedFunction", object : TypeToken<Database>() {}.type),
    DOCUMENT("docs", "Document", object : TypeToken<Document>() {}.type),
    ATTACHMENT("attachments", "Attachment", object : TypeToken<Database>() {}.type),
    OFFER("offers", "Offer", object : TypeToken<Offer>() {}.type);

    val listName: String = "${fullname}s"
}