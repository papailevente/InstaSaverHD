package com.example.instasaverhd

import com.example.instasaverhd.data.repository.UpdateManager
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateManagerTest {

    @Test
    fun isVersionNewer_newerMajorVersion_returnsTrue() {
        assertTrue(UpdateManager.isVersionNewer("2.0.0", "1.2.0"))
    }

    @Test
    fun isVersionNewer_newerMinorVersion_returnsTrue() {
        assertTrue(UpdateManager.isVersionNewer("1.3.0", "1.2.0"))
    }

    @Test
    fun isVersionNewer_newerPatchVersion_returnsTrue() {
        assertTrue(UpdateManager.isVersionNewer("1.2.1", "1.2.0"))
    }

    @Test
    fun isVersionNewer_sameVersion_returnsFalse() {
        assertFalse(UpdateManager.isVersionNewer("1.2.0", "1.2.0"))
    }

    @Test
    fun isVersionNewer_olderVersion_returnsFalse() {
        assertFalse(UpdateManager.isVersionNewer("1.1.0", "1.2.0"))
    }

    @Test
    fun isVersionNewer_handlesPrefixV() {
        assertTrue(UpdateManager.isVersionNewer("v1.3.0".trimStart('v'), "v1.2.0".trimStart('v')))
    }
}
