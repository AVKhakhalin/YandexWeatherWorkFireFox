package com.example.yandexweatherwork.repository

import android.util.Base64

class DecoderYandexKey(val encodedText: String) {
    fun decodedKey(): String {
        if (encodedText != null) {
            val decoded: ByteArray = Base64.decode(encodedText, Base64.DEFAULT)
            return decoded?.let { String(it) }
        } else {
            return ""
        }
    }
}