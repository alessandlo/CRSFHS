package com.example.crsfhs.android.services

import java.security.MessageDigest

object Encryption {
    /**
     * Erstellt Hash mit SHA512
     */
    fun String.toSHA(): String {
        val digest = MessageDigest.getInstance("SHA-512").digest(this.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}
