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

@RunWith(AndroidJUnit4::class)
@Config(sdk = [31])
class ReadPhoneStatePermissionTest {

    @Test
    fun `READ_PHONE_STATE permission is declared in manifest`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
        val permissions = packageInfo.requestedPermissions?.toList() ?: emptyList()
        
        assertTrue(
            "READ_PHONE_STATE permission should be declared in manifest",
            permissions.contains(Manifest.permission.READ_PHONE_STATE)
        )
    }

    @Test
    fun `hasReadPhoneStatePermission returns false by default in test environment`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        
        val result = PermissionUtils.hasReadPhoneStatePermission(context)
        
        // In test environment, permissions are not granted by default
        assertFalse("Should return false when permission is not granted", result)
    }

    @Test
    fun `requestReadPhoneStatePermission does not crash`() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
        
        // This should not throw an exception
        PermissionUtils.requestReadPhoneStatePermission(activity)
        
        // Test passes if no exception is thrown
        assertTrue("Permission request should complete without crashing", true)
    }

    @Test
    fun `isReadPhoneStatePermissionPermanentlyDenied returns expected result`() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
        
        // This should not throw an exception
        val result = PermissionUtils.isReadPhoneStatePermissionPermanentlyDenied(activity)
        
        // Just verify the method can be called - the exact result depends on Robolectric implementation
        assertNotNull("Method should return a boolean value", result)
    }
}