package com.example.instasaverhd.utils

object InstagramUrlParser {
    private val instagramUrlRegex = Regex("""https?://(?:www\.)?(?:instagram\.com|instagr\.am)/[^\s]+""", RegexOption.IGNORE_CASE)

    fun isValidUrl(url: String): Boolean {
        val trimmed = url.trim()
        if (trimmed.isBlank()) return false
        return trimmed.startsWith("http://") || trimmed.startsWith("https://")
    }

    fun isInstagramUrl(url: String): Boolean {
        val trimmed = url.trim().lowercase()
        return trimmed.contains("instagram.com") || trimmed.contains("instagr.am")
    }

    fun cleanUrl(input: String): String {
        val trimmed = input.trim()
        val matchedUrl = instagramUrlRegex.find(trimmed)?.value ?: trimmed
        return matchedUrl.split("?").firstOrNull() ?: matchedUrl
    }
}
