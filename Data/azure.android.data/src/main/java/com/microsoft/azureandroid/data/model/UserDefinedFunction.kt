package com.microsoft.azureandroid.data.model

/**
 * Created by Nate Rickard on 11/24/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

/**
 * Represents a user defined function in the Azure Cosmos DB service.
 * 
 *  - Remark:
 *    Azure Cosmos DB supports JavaScript user defined functions (UDFs) which are stored in
 *    the database and can be used inside queries.
 *    Refer to [javascript-integration](http://azure.microsoft.com/documentation/articles/documentdb-sql-query/#javascript-integration) for how to use UDFs within queries.
 *    Refer to [udf](http://azure.microsoft.com/documentation/articles/documentdb-programming/#udf) for more details about implementing UDFs in JavaScript.
 */
class UserDefinedFunction : Resource() {

    /**
     * Gets or sets the body of the user defined function for the Azure Cosmos DB service.
     * 
     *  - Remark:
     *    This must be a valid JavaScript function
     * 
     *  - Example:
     *    `"function (input) { return input.toLowerCase(); }"`
     */
    var body: String? = null
}