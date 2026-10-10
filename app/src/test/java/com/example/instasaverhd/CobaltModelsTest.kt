package com.example.instasaverhd

import com.example.instasaverhd.data.remote.CobaltResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CobaltModelsTest {

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Test
    fun parseTunnelResponse_success() {
        val json = """
            {
                "status": "tunnel",
                "url": "https://stream.cobalt.tools/media123.mp4",
                "filename": "instagram_reel.mp4"
            }
        """.trimIndent()

        val adapter = moshi.adapter(CobaltResponse::class.java)
        val response = adapter.fromJson(json)

        assertNotNull(response)
        assertEquals("tunnel", response?.status)
        assertEquals("https://stream.cobalt.tools/media123.mp4", response?.url)
        assertEquals("instagram_reel.mp4", response?.filename)
    }

    @Test
    fun parsePickerResponse_success() {
        val json = """
            {
                "status": "picker",
                "picker": [
                    {
                        "type": "video",
                        "url": "https://stream.cobalt.tools/video1.mp4",
                        "thumb": "https://stream.cobalt.tools/thumb1.jpg"
                    }
                ]
            }
        """.trimIndent()

        val adapter = moshi.adapter(CobaltResponse::class.java)
        val response = adapter.fromJson(json)

        assertNotNull(response)
        assertEquals("picker", response?.status)
        assertEquals(1, response?.picker?.size)
        assertEquals("video", response?.picker?.get(0)?.type)
        assertEquals("https://stream.cobalt.tools/video1.mp4", response?.picker?.get(0)?.url)
    }

    @Test
    fun parseErrorResponse_success() {
        val json = """
            {
                "status": "error",
                "text": "link invalid"
            }
        """.trimIndent()

        val adapter = moshi.adapter(CobaltResponse::class.java)
        val response = adapter.fromJson(json)

        assertNotNull(response)
        assertEquals("error", response?.status)
        assertEquals("link invalid", response?.text)
    }
}
