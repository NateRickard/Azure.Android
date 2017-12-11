package com.microsoft.azureandroid.data.model

/**
 * Created by Nate Rickard on 12/11/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

enum class ErrorType(val message: String) {

    SetupError("AzureData is not setup.  Must call AzureData.setup() before attempting CRUD operations on resources."),
    InvalidId("Cosmos DB Resource IDs must not exceed 255 characters and cannot contain whitespace"),
    IncompleteIds("This resource is missing the selfLink and/or resourceId properties.  Use an override that takes parent resource or ids instead"),
    UnknownError("An unknown error occured."),
    InternalError("An internal error occured.")
}