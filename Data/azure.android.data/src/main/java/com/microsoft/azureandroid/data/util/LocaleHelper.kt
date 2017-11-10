package com.microsoft.azureandroid.data.util

import android.content.Context
import android.os.Build
import java.util.*

/**
 * Created by nater on 11/9/17.
 */

class LocaleHelper {

    companion object {

        fun getCurrentLocale(context: Context): Locale {

            //ContextProvider.appContext.assets.locales   //app/system locale list based on resources/assets

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.resources.configuration.locales[0]
            } else {
                /*
                This field was deprecated in API level 24. Do not set or read this directly.
                Use getLocales() and setLocales(LocaleList). If only the primary locale is needed,
                getLocales().get(0) is now the preferred accessor
                */
                context.resources.configuration.locale
            }
        }
    }
}