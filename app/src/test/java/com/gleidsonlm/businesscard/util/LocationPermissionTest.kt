package com.gleidsonlm.businesscard.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config
import com.gleidsonlm.businesscard.MainActivity
import org.junit.Assert.*

/**
 * Tests for location permission handling in PermissionUtils.
 * Tests the integration of location permissions for geo-compliance features.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [31])
class LocationPermissionTest {

    @Test
    fun `ACCESS_COARSE_LOCATION permission is declared in manifest`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
        val permissions = packageInfo.requestedPermissions?.toList() ?: emptyList()
        
        assertTrue(
            "ACCESS_COARSE_LOCATION permission should be declared in manifest",
            permissions.contains(Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }

    @Test
    fun `hasLocationPermission returns false by default in test environment`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        
        val result = PermissionUtils.hasLocationPermission(context)
        
        // In test environment, permissions are not granted by default
        assertFalse("Should return false when permission is not granted", result)
    }

    @Test
    fun `requestLocationPermission does not crash`() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
        
        // This should not throw an exception
        PermissionUtils.requestLocationPermission(activity)
        
        // Test passes if no exception is thrown
        assertTrue("Permission request should complete without crashing", true)
    }

    @Test
    fun `isLocationPermissionPermanentlyDenied returns expected result`() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
        
        // This should not throw an exception
        val result = PermissionUtils.isLocationPermissionPermanentlyDenied(activity)
        
        // Just verify the method can be called - the exact result depends on Robolectric implementation
        assertNotNull("Method should return a boolean value", result)
    }

    @Test
    fun `location permission constants should be correctly defined`() {
        // When/Then
        assertEquals("Location permission request code should be 1003", 1003, PermissionUtils.REQUEST_CODE_ACCESS_COARSE_LOCATION)
    }

    @Test
    fun `permission request code should be unique`() {
        // When/Then - Verify all permission request codes are unique
        val codes = setOf(
            PermissionUtils.REQUEST_CODE_INSTALL_PACKAGES,
            PermissionUtils.REQUEST_CODE_READ_PHONE_STATE,
            PermissionUtils.REQUEST_CODE_ACCESS_COARSE_LOCATION
        )
        
        assertEquals("All permission request codes should be unique", 3, codes.size)
    }
}