package com.microsoft.azureandroid.data.model

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.microsoft.azureandroid.data.util.JsonHelper

/**
 * Created by nater on 11/3/17.
 */

class ResourceList<T: Resource>(resourceType: ResourceType, json: JsonObject) {

    var resourceId: String? = null
    var count: Int? = null

    lateinit var items: ArrayList<T>

//    init?(_ resourceType: String, json dict: [String:Any]) {
//        if let resourceId   = dict[resourceIdKey]   as? String { self.resourceId = resourceId } else { return nil }
//        count       = dict[countKey]        as? Int ?? 0
//        if let resourceDicts = dict[resourceType] as? [[String:Any]] {
//            for resourceDict in resourceDicts {
//                if let resource = T(fromJson: resourceDict) {
//                self.items.append(resource)
//            }
//            }
//        }
//    }

    init {
        resourceId  = json[resourceIdKey]?.asString
        count       = json[countKey]?.asInt

//        val resourceType = object : TypeToken<T: Resource>() {}.type

        json[resourceType.listName]?.asJsonArray?.let {

            items = ArrayList(it.size())

            for (item in it) {
//                val resource = type.newInstance()
                val resource = JsonHelper.Gson.fromJson<T>(item, resourceType.type)
                items.add(resource)
            }
        }
    }

    val isPopuated: Boolean = resourceId != null

    companion object {

        const val resourceIdKey   = "_rid"
        const val countKey        = "_count"
    }
}