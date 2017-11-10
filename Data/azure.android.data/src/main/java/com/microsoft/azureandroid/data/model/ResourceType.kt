package com.microsoft.azureandroid.data.model

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by nater on 10/31/17.
 */

enum class ResourceType(val path: String, val fullname: String, val type: Type) {

    DATABASE("dbs", "Database", object : TypeToken<Database>() {}.type),
    USER("users", "User", object : TypeToken<Database>() {}.type),
    PERMISSION("permissions", "Permission", object : TypeToken<Database>() {}.type),
    COLLECTION("colls", "DocumentCollection", object : TypeToken<Database>() {}.type),
    STORED_PROCEDURE("sprocs", "StoredProcedure", object : TypeToken<Database>() {}.type),
    TRIGGER("triggers", "Trigger", object : TypeToken<Database>() {}.type),
    UDF("udfs", "UserDefinedFunction", object : TypeToken<Database>() {}.type),
    DOCUMENT("docs", "Document", object : TypeToken<Document>() {}.type),
    ATTACHMENT("attachments", "Attachment", object : TypeToken<Database>() {}.type),
    OFFER("offers", "Offer", object : TypeToken<Database>() {}.type);

    val listName: String = "${fullname}s"
}