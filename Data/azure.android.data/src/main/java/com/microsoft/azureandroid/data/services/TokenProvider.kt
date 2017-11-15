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

    //    let dateFormatter: DateFormatter = {
//
//        let formatter = DateFormatter()
//        formatter.locale = Locale(identifier: "en_US_POSIX")
//        formatter.dateFormat = "E, dd MMM yyyy HH:mm:ss zzz"
//        formatter.timeZone = TimeZone(secondsFromGMT: 0)
//
//        return formatter
//    }()

    private val dateFormatter : SimpleDateFormat


    init {
        dateFormatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss")
        dateFormatter.timeZone = TimeZone.getTimeZone("GMT")
    }

    // https://docs.microsoft.com/en-us/rest/api/documentdb/access-control-on-documentdb-resources#constructkeytoken
    fun getToken(verb: ApiValues.HttpMethod, resourceType: ResourceType, resourceLink: String) : Token {

        val dateString = String.format("%s %s", dateFormatter.format(Date()), "GMT")
//        val dateString = dateFormatter.string(from: Date())

        val payload = String.format("%s\n%s\n%s\n%s\n%s\n",
                verb.name.toLowerCase(Locale.ROOT),
                resourceType.path.toLowerCase(Locale.ROOT),
                resourceLink,
                dateString.toLowerCase(Locale.ROOT), "")

//        val payload = "${httpMethod.toLowerCase()}\n${resourceType.toLowerCase()}\n$resourceLink\n${dateString.toLowerCase()}\n\n"

        print(payload)

        val signature = hmac(payload) // .hmac(algorithm: .SHA256, identifier: identifier)

//        val authString = "type=$keyType&ver=$tokenVersion&sig=$signature"

        val authStringEncoded = URLEncoder.encode(String.format("type=%s&ver=%s&sig=%s", keyType, tokenVersion, signature), "UTF-8")

//        val authStringEncoded = authString.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!

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