package com.microsoft.azureandroid.data.model

/**
 * Created by nater on 11/7/17.
 */

class Result<out T: Resource>(val resource: T? = null, val error: Error? = null) {


}