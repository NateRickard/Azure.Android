package com.microsoft.azureandroid.data.services

import android.util.Base64
import com.microsoft.azureandroid.data.constants.ApiValues
import com.microsoft.azureandroid.data.constants.HmacAlgorithm
import com.microsoft.azureandroid.data.constants.TokenType
import com.microsoft.azureandroid.data.model.ResourceType
import com.microsoft.azureandroid.data.model.Token
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
* Created by Nate Rickard on 11/2/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class TokenProvider(private var key: String, private var keyType: TokenType = TokenType.MASTER, private var tokenVersion: String = "1.0") {

    private val dateFormatter : SimpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ROOT)

    init {
        dateFormatter.timeZone = TimeZone.getTimeZone("GMT")
    }

    // https://docs.microsoft.com/en-us/rest/api/documentdb/access-control-on-documentdb-resources#constructkeytoken
    fun getToken(verb: ApiValues.HttpMethod, resourceType: ResourceType, resourceLink: String) : Token {

        val dateString = String.format("%s %s", dateFormatter.format(Date()), "GMT")

        val payload = String.format("%s\n%s\n%s\n%s\n%s\n",
                verb.name.toLowerCase(Locale.ROOT),
                resourceType.path.toLowerCase(Locale.ROOT),
                resourceLink,
                dateString.toLowerCase(Locale.ROOT), "")

        print(payload)

        val signature = hmac(payload)

        val authStringEncoded = URLEncoder.encode(String.format("type=%s&ver=%s&sig=%s", keyType, tokenVersion, signature), "UTF-8")

        return Token(authStringEncoded, dateString)
    }

    private fun hmac(string: String, algorithm: HmacAlgorithm = HmacAlgorithm.SHA256): String? {
        try {
            val decodedKey = org.apache.commons.codec.binary.Base64.decodeBase64(key.toByteArray())

            val hmac = Mac.getInstance(algorithm.value)
            val keySpec = SecretKeySpec(decodedKey, algorithm.value)
            hmac.init(keySpec)

            val hashPayLoad = hmac.doFinal(string.toByteArray(charset("UTF-8")))

            return Base64.encodeToString(hashPayLoad, Base64.DEFAULT).replace("\n", "")

        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}