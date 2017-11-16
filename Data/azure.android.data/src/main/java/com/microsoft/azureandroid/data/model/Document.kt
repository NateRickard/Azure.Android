package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
* Created by Nate Rickard on 10/31/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

open class Document : Resource() {

//    var baseKeys: List<String> = listOf(idKey, resourceIdKey, selfLinkKey, etagKey, timestampKey, attachmentsLinkKey)

    @SerializedName(attachmentsLinkKey)
    var attachmentsLink: String? = null

    var data: Map<String, Any>? = null


//    override init() { super.init() }

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