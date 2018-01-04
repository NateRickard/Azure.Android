package com.microsoft.azureandroid.data.util

import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.getOrSet

/**
 * Created by Nate Rickard on 12/22/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

// https://docs.microsoft.com/en-us/dotnet/standard/base-types/standard-date-and-time-format-strings#Roundtrip
internal class RoundtripDateConverter {

    companion object {

        private const val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZZZZZ"

        // formatted length will actually be off a bit due to escaping, etc. e.g. "2017-12-22T17:25:11.5710000+00:00"
        const val formattedDateLength = dateFormat.length - 1

        private fun createDateFormatter() : SimpleDateFormat {

            val format = SimpleDateFormat(dateFormat, Locale("en", "US", "POSIX"))
            format.timeZone = TimeZone.getTimeZone("UTC")

            return format
        }

        private val formatters: ThreadLocal<SimpleDateFormat> = ThreadLocal()

        private val dateFormatter by lazy {
            formatters.getOrSet {
                createDateFormatter()
            }
        }

        fun toString(date: Date?) : String? {

            if (date == null) {
                return null
            }

            return dateFormatter.format(date)
        }

        fun toDate(dateString: String?) : Date? {

            if (dateString == null) {
                return null
            }

            return dateFormatter.parse(dateString)
        }
    }
}