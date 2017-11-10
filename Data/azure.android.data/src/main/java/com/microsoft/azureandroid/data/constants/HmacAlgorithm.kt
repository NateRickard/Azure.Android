package com.microsoft.azureandroid.data.constants

/**
 * Created by nater on 11/2/17.
 */

enum class HmacAlgorithm(val value: String) {
    MD5("HmacMD5"),
    SHA1("HmacSHA1"),
    SHA224("HmacSHA224"),
    SHA256 ("HmacSHA256"),
    SHA384("HmacSHA384"),
    SHA512("HmacSHA512");
}