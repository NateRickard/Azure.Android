package com.microsoft.azureandroid.data.model

/**
 * Created by Nate Rickard on 1/3/18.
 * Copyright © 2018 Nate Rickard. All rights reserved.
 */

class DictionaryDocument(id: String? = null) : Document(id) {

    @Transient
    internal var data = DocumentDataMap()

    // will be mapped to indexer, i.e. doc[key]
    operator fun get(key: String) = data[key]

    // will be mapped to indexer, i.e. doc[key] = value
    operator fun set(key: String, value: Any?) {

        if (Keys.list.contains(key)) {
            throw Exception("Error: Cannot use [key] = value syntax to set the following system generated properties: ${Keys.list.joinToString()}")
        }

        data[key] = value
    }
}