package com.example.instasaverhd

import com.example.instasaverhd.utils.InstagramUrlParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InstagramUrlParserTest {

    @Test
    fun isValidUrl_validHttpAndHttps_returnsTrue() {
        assertTrue(InstagramUrlParser.isValidUrl("https://www.instagram.com/reel/C123456/"))
        assertTrue(InstagramUrlParser.isValidUrl("http://instagr.am/p/C67890/"))
    }

    @Test
    fun isValidUrl_invalidString_returnsFalse() {
        assertFalse(InstagramUrlParser.isValidUrl(""))
        assertFalse(InstagramUrlParser.isValidUrl("   "))
        assertFalse(InstagramUrlParser.isValidUrl("ftp://instagram.com"))
    }

    @Test
    fun isInstagramUrl_detectsInstagramDomains() {
        assertTrue(InstagramUrlParser.isInstagramUrl("https://www.instagram.com/reel/C123/"))
        assertTrue(InstagramUrlParser.isInstagramUrl("https://instagr.am/p/C123/"))
        assertFalse(InstagramUrlParser.isInstagramUrl("https://www.youtube.com/watch?v=123"))
    }

    @Test
    fun cleanUrl_removesQueryParameters() {
        val rawUrl = "https://www.instagram.com/reel/C123456/?utm_source=ig_web_copy_link&igsh=MzRlODBiNWFlZA=="
        val expected = "https://www.instagram.com/reel/C123456/"
        assertEquals(expected, InstagramUrlParser.cleanUrl(rawUrl))
    }
}
