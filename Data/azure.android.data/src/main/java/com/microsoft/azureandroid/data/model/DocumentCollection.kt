package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
* Created by Nate Rickard on 11/11/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

/**
 * Represents a document collection in the Azure Cosmos DB service.
 * A collection is a named logical container for documents.
 *
 * - Remark:
 *   A database may contain zero or more named collections and each collection consists of zero or more Json documents.
 *   Being schema-free, the documents in a collection do not need to share the same structure or fields.
 *   Since collections are application resources, they can be authorized using either the master key or resource keys.
 *   Refer to [collections](http://azure.microsoft.com/documentation/articles/documentdb-resources/#collections) for more details on collections.
 */
class DocumentCollection : Resource() {

    /**
     * Gets the self-link for conflicts in a collection from the Azure Cosmos DB service.
     */
    @SerializedName(conflictsLinkKey)
    var conflictsLink: String? = null

    /**
     * Gets the default time to live in seconds for documents in a collection from the Azure Cosmos DB service.
     */
    var defaultTimeToLive: Int? = null

    /**
     * Gets the self-link for documents in a collection from the Azure Cosmos DB service.
     */
    @SerializedName(documentsLinkKey)
    var documentsLink: String? = null

    /**
     * Gets the `IndexingPolicy` associated with the collection from the Azure Cosmos DB service.
     */
    var indexingPolicy: IndexingPolicy? = null

    /**
     * Gets or sets `PartitionKeyDefinition` object in the Azure Cosmos DB service.
     */
    var partitionKey: PartitionKeyDefinition? = null

    /**
     * Gets the self-link for stored procedures in a collection from the Azure Cosmos DB service.
     */
    @SerializedName(storedProceduresLinkKey)
    var storedProceduresLink: String? = null

    /**
     * Gets the self-link for triggers in a collection from the Azure Cosmos DB service.
     */
    @SerializedName(triggersLinkKey)
    var triggersLink: String? = null

    /**
     * Gets the self-link for user defined functions in a collection from the Azure Cosmos DB service.
     */
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
        const val storedProceduresLinkKey         = "_sprocs"
        const val triggersLinkKey                 = "_triggers"
        const val userDefinedFunctionsLinkKey     = "_udfs"
    }


    /**
     * Represents the indexing policy configuration for a collection in the Azure Cosmos DB service.
     */
    class IndexingPolicy {

        /**
         * Gets or sets a value that indicates whether automatic indexing is enabled for a collection in
         * the Azure Cosmos DB service.
         */
        var automatic: Boolean = false

        /**
         * Gets or sets the collection containing `ExcludedPath` objects in the Azure Cosmos DB service.
         */
        var excludedPaths: Array<ExcludedPath>? = null

        /**
         * Gets or sets the collection containing `IncludedPath` objects in the Azure Cosmos DB service.
         */
        var includedPaths: Array<IncludedPath>? = null

        /**
         * Gets or sets the indexing mode (`.consistent` or `.lazy`) in the Azure Cosmos DB service.
         */
        var indexingMode: IndexingMode? = null


        /**
         * Specifies a path within a Json document to be excluded while indexing data for the Azure Cosmos DB service.
         */
        class ExcludedPath {

            /**
             * Gets or sets the path to be excluded from indexing in the Azure Cosmos DB service.
             */
            var path: String? = null
        }


        /**
         * Specifies a path within a Json document to be included in the Azure Cosmos DB service.
         */
        class IncludedPath {

            /**
             * Gets or sets the path to be indexed in the Azure Cosmos DB service.
             */
            var path: String? = null

            /**
             * Gets or sets the collection of `Index` objects to be applied for this included path in
             * the Azure Cosmos DB service.
             */
            var indexes: Array<Index>? = null

            /**
             * Base class for `IndexingPolicy` `Indexes` in the Azure Cosmos DB service,
             * you should use a concrete `Index` like `HashIndex` or `RangeIndex`.
             */
            class Index (

                /**
                 * Gets or sets the kind of indexing to be applied in the Azure Cosmos DB service.
                 */
                var kind: IndexKind? = null,

                /**
                 * Specifies the target data type for the index path specification.
                 */
                var dataType: DataType? = null,

                /**
                 * Specifies the precision to be used for the data type associated with this index.
                 */
                var precision: Short? = null
            ) {

                /**
                 * These are the indexing types available for indexing a path in the Azure Cosmos DB service.
                 *
                 * - hash:     The index entries are hashed to serve point look up queries.
                 * - range:    The index entries are ordered. Range indexes are optimized for
                 *             inequality predicate queries with efficient range scans.
                 * - spatial:  The index entries are indexed to serve spatial queries.
                 */
                enum class IndexKind {

                    Hash,
                    Range,
                    Spatial
                }

                /**
                 * Defines the target data type of an index path specification in the Azure Cosmos DB service.
                 *
                 * - lineString:   Represent a line string data type.
                 * - number:       Represent a numeric data type.
                 * - point:        Represent a point data type.
                 * - polygon:      Represent a polygon data type.
                 * - string:       Represent a string data type.
                 */
                enum class DataType {

                    LineString,
                    Number,
                    Point,
                    Polygon,
                    String
                }

                companion object {

                    /**
                     * Returns an instance of the `HashIndex` class with specified `DataType` (and precision) for
                     * the Azure Cosmos DB service.
                     */
                    fun hash(dataType: DataType, precision: Short? = null) : Index =
                            Index(IndexKind.Hash, dataType, precision)

                    /**
                     * Returns an instance of the `RangeIndex` class with specified `DataType` (and precision) for
                     * the Azure Cosmos DB service.
                     */
                    fun range(dataType: DataType, precision: Short? = null) : Index =
                            Index(IndexKind.Range, dataType, precision)

                    /**
                     * Returns an instance of the `RangeIndex` class with specified `DataType` (and precision) for
                     * the Azure Cosmos DB service.
                     */
                    fun spatial(dataType: DataType) : Index =
                            Index(IndexKind.Spatial, dataType)
                }
            }
        }

        /**
         * Specifies the supported indexing modes in the Azure Cosmos DB service.
         *
         * - consistent:   Index is updated synchronously with a create, update or delete operation.
         * - lazy:         Index is updated asynchronously with respect to a create, update or delete operation.
         * - none:         No index is provided.
         */
        enum class IndexingMode {

            @SerializedName("consistent")
            Consistent,
            @SerializedName("lazy")
            Lazy,
            @SerializedName("none")
            None
        }
    }

    /**
     * Specifies a partition key definition for a particular path in the Azure Cosmos DB service.
     */
    class PartitionKeyDefinition {

        /**
         * Gets or sets the paths to be partitioned in the Azure Cosmos DB service.
         */
        var paths: Array<String>? = null

        /**
         * The algorithm used for partitioning. Only Hash is supported.
         */
        val kind = "Hash"
    }
}