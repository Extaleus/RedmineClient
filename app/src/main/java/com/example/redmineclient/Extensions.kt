package com.example.redmineclient

import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder

class Extensions {
    companion object {
        fun encodeBase64(data: Any?): String {
            val jsonString = Gson().toJson(data)
            return URLEncoder.encode(jsonString, "utf-8")
        }

        inline fun <reified T> decodeBase64(string: String): T {
            val decodedJsonString = URLDecoder.decode(string, "utf-8")
            return Gson().fromJson(decodedJsonString, T::class.java)
        }
    }
}