package com.microsoft.azureandroid.data.model

/**
 * Created by Nate Rickard on 11/23/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

/** Represents a stored procedure in the Azure Cosmos DB service.
*
* - Remark:
*   Azure Cosmos DB allows application logic written entirely in JavaScript to be executed directly inside
*   the database engine under the database transaction.
*   For additional details, refer to the server-side JavaScript API documentation.
*/
class StoredProcedure : Resource() {

    /** Gets or sets the body of the Azure Cosmos DB stored procedure.
    ///
    /// - Remark:
    ///   Must be a valid JavaScript function.
    ///
    /// - Example:
    ///   `"function () { getContext().getResponse().setBody('Hello World!'); }`
    */
    var body: String? = null

    companion object {

        const val resourceName = "StoredProcedure"
        const val listName = "StoredProcedures"
    }
}