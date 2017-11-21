package com.microsoft.azureandroid.data.model

/**
* Created by Nate Rickard on 11/7/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class ListResult<T: Resource>(val resource: ResourceList<T>? = null, val error: DataError? = null)