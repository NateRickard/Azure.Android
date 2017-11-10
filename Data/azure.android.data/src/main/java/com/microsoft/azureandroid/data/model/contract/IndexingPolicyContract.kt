package com.microsoft.azureandroid.data.model.contract

import java.util.ArrayList

/**
 * Created by nater on 11/2/17.
 */

class IndexingPolicyContract {
    var indexingMode: String? = null

    var automatic: Boolean = false

    var includedPaths: ArrayList<IncludedPathContract>? = null

    var excludedPaths: ArrayList<Any>? = null
}