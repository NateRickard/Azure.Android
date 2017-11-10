package com.microsoft.azureandroid.data.model.contract

import java.util.ArrayList

/**
 * Created by nater on 11/2/17.
 */

class CollectionsContract {
    var rid: String? = null

    var documentCollections: ArrayList<DocumentCollectionContract>? = null

    var count: Int = 0
}