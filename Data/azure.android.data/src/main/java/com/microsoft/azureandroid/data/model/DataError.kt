package com.microsoft.azureandroid.data.model

/**
 * Created by nater on 11/7/17.
 */

class DataError(message: String?, val code: Int? = null) : Error(message) {

    constructor(error: Error) : this(error.message)

    constructor(error: Exception) : this(error.message)

    constructor() : this("")

    override fun toString(): String {

        return "\r\nError\r\n\t$message\r\n${if (code != null) "\t$code\r\n" else ""}"

    }
}