package com.microsoft.azureandroid.data.model.indexing

/**
 * Created by Nate Rickard on 2/1/18.
 * Copyright © 2018 Nate Rickard. All rights reserved.
 */

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