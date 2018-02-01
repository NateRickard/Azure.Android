package com.microsoft.azureandroid.data.model.indexing

/**
 * Created by Nate Rickard on 2/1/18.
 * Copyright © 2018 Nate Rickard. All rights reserved.
 */

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