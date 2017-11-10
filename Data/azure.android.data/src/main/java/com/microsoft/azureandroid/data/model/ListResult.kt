package com.microsoft.azureandroid.data.model

/**
 * Created by nater on 11/7/17.
 */

class ListResult<T: Resource>(val resource: ResourceList<T>? = null, val error: Error? = null) {

}