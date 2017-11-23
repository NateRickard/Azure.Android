package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
* Created by Nate Rickard on 10/31/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

open class Document : Resource() {

    // Gets the self-link corresponding to attachments of the document from the Azure Cosmos DB service.
    @SerializedName(attachmentsLinkKey)
    var attachmentsLink: String? = null

    // Gets or sets the time to live in seconds of the document in the Azure Cosmos DB service.
    var timeToLive: Int? = null

    val data: MutableMap<String, Any?> = mutableMapOf()

    // will be mapped to indexer, i.e. doc[key]
    operator fun get(key: String): Any? = data[key]

    // will be mapped to indexer, i.e. doc[key] = value
    operator fun set(key: String, value: Any?) {

        if (this.javaClass != Document::class.java) {
            throw Exception("Error: Indexing operation cannot be used on a child type of Document")
        }

        if (sysKeys.contains(key) || key == attachmentsLinkKey) {
            throw Exception("Error: Cannot use [key] = value syntax to set the following system generated properties: ${sysKeys.joinToString()}")
        }

        data[key] = value
    }

//    init {
//        super.sysKeys.add(attachmentsLinkKey)
//    }

//    override init(_ id: String) { super.init(id) }

//    required public init?(fromJson dict: [String:Any]) {
//        super.init(fromJson: dict)
//
//        attachmentsLink = dict[ADDocument.attachmentsLinkKey] as? String
//
//        data = dict.filter{ x in !baseKeys.contains(x.key) }
//    }

//    override var dictionary: [String : Any] {
//        return super.dictionary.merging([
//        ADDocument.attachmentsLinkKey:attachmentsLink.valueOrEmpty])
//        { (_, new) in new }.merging(data)
//        { (_, new) in new }
//    }

//    public subscript(key: String) -> Any? {
//        get {
//            return data[key]
//        }
//        set {
//
//            if newValue == nil { data[key] = newValue; return }
//
//            assert(!baseKeys.contains(key), "Error: Subscript cannot be used to set the following system generated properties: \(baseKeys.joined(separator: ", "))\n")
//
//            if let date = newValue as? Date {
//
//                data[key] = date.timeIntervalSince1970
//
//            } else {
//
//                assert(newValue is NSString || newValue is NSNumber || newValue is NSNull, "Error: Value for `\(key)` is not a primitive type.  Only primitive types can be stored with subscript.\n")
//
//                data[key] = newValue
//            }
//        }
//    }

    companion object {

        const val attachmentsLinkKey   = "_attachments"
    }
}