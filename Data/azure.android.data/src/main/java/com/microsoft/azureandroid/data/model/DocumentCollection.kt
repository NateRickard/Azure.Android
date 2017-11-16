package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
* Created by Nate Rickard on 11/11/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class DocumentCollection : Resource() {

    @SerializedName(conflictsLinkKey)
    var conflictsLink: String? = null

    @SerializedName(documentsLinkKey)
    var documentsLink: String? = null

    @SerializedName(indexingPolicyKey)
    var indexingPolicy: IndexingPolicy? = null

    var partitionKey: PartitionKeyDefinition? = null

    @SerializedName(storedProceduresLinkKey)
    var storedProceduresLink: String? = null

    @SerializedName(triggersLinkKey)
    var triggersLink: String? = null

    @SerializedName(userDefinedFunctionsLinkKey)
    var userDefinedFunctionsLink: String? = null

    fun childLink (resourceId: String? = null) : String {

        return if (resourceId == null || resourceId == "") {
            selfLink!!.split("/").last().toLowerCase()
        }
        else {
            resourceId.toLowerCase()
        }
    }

    companion object {

        const val conflictsLinkKey                = "_conflicts"
        const val documentsLinkKey                = "_docs"
        const val indexingPolicyKey               = "indexingPolicy"
        const val storedProceduresLinkKey         = "_sprocs"
        const val triggersLinkKey                 = "_triggers"
        const val userDefinedFunctionsLinkKey     = "_udfs"
    }


    class IndexingPolicy {

        var automatic: Boolean = false

        var excludedPaths: Array<ExcludedPath>? = null

        var includedPaths: Array<IncludedPath>? = null

        var indexingMode: IndexMode? = null


        class ExcludedPath {

            var path: String? = null
        }

        class IncludedPath {

            var path: String? = null

            var indexes: Array<Index>? = null

            class Index {

                var kind: IndexKind? = null

                var dataType: DataType? = null

                var precision: Short? = null

                enum class IndexKind {

                    Hash,
                    Range,
                    Spatial
                }

                enum class DataType {

                    LineString,
                    Number,
                    Point,
                    Polygon,
                    String
                }
            }
        }

        enum class IndexMode {

            @SerializedName("consistent")
            Consistent,
            @SerializedName("lazy")
            Lazy,
            @SerializedName("none")
            None
        }
    }

    class PartitionKeyDefinition {

        var paths: Array<String>? = null

        val kind = "Hash" // The algorithm used for partitioning. Only Hash is supported.
    }
}