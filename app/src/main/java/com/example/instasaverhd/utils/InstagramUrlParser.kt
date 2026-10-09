package com.example.instasaverhd.utils

object InstagramUrlParser {
    fun isValidUrl(url: String): Boolean {
        val trimmed = url.trim()
        if (trimmed.isBlank()) return false
        return trimmed.startsWith("http://") || trimmed.startsWith("https://")
    }

    fun isInstagramUrl(url: String): Boolean {
        val trimmed = url.trim().lowercase()
        return trimmed.contains("instagram.com") || trimmed.contains("instagr.am")
    }

    fun cleanUrl(url: String): String {
        return url.trim().split("?").firstOrNull() ?: url.trim()
    }
}
