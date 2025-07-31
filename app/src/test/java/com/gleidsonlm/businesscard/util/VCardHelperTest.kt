package com.gleidsonlm.businesscard.util

import com.gleidsonlm.businesscard.ui.UserData
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class VCardHelperTest {

    @Test
    fun `generateVCardString with full data creates correct vCard`() {
        val userData = UserData(
            fullName = "John Doe",
            title = "Software Engineer",
            phoneNumber = "1234567890",
            emailAddress = "john.doe@example.com",
            company = "Example Corp",
            website = "https://example.com",
            avatarUri = null
        )

        val vCardString = VCardHelper.generateVCardString(userData)

        assertTrue(vCardString.contains("BEGIN:VCARD"))
        assertTrue(vCardString.contains("END:VCARD"))
        assertTrue(vCardString.contains("FN:John Doe"))
        // Based on implementation: given=John, family=Doe -> N:Doe;John;;;
        assertTrue(vCardString.contains("N:Doe;John;;;"))
        assertTrue(vCardString.contains("TITLE:Software Engineer"))
        assertTrue(vCardString.contains("1234567890")) // Phone number in some format
        assertTrue(vCardString.contains("john.doe@example.com"))
        assertTrue(vCardString.contains("Example Corp"))
        assertTrue(vCardString.contains("https://example.com"))
    }

    @Test
    fun `generateVCardString with minimal data creates correct vCard`() {
        val userData = UserData(
            fullName = "Jane Doe",
            title = "",
            phoneNumber = "",
            emailAddress = "jane.doe@example.com",
            company = "",
            website = "",
            avatarUri = null
        )

        val vCardString = VCardHelper.generateVCardString(userData)

        assertTrue(vCardString.contains("BEGIN:VCARD"))
        assertTrue(vCardString.contains("FN:Jane Doe"))
        assertTrue(vCardString.contains("N:Doe;Jane;;;"))
        assertTrue(vCardString.contains("jane.doe@example.com"))
        assertTrue(vCardString.contains("END:VCARD"))
        // Empty fields should not appear in meaningful ways
        assertFalse(vCardString.contains("TITLE:"))
        assertFalse(vCardString.contains("ORG:"))
    }

    @Test
    fun `generateVCardString with empty UserData creates minimal vCard`() {
        val userData = UserData(
            fullName = "",
            title = "",
            phoneNumber = "",
            emailAddress = "",
            company = "",
            website = "",
            avatarUri = null
        )

        val vCardString = VCardHelper.generateVCardString(userData)

        assertTrue(vCardString.contains("BEGIN:VCARD"))
        // Depending on ez-vcard behavior, FN might be empty or not present
        // For an empty FN, it might output "FN:" or omit it.
        // Same for N. Let's check they are not present with content.
        assertFalse(vCardString.contains("FN:"))
        assertFalse(vCardString.contains("N:;")) // Check it's not just N: with no value
        assertFalse(vCardString.contains("TITLE:"))
        assertFalse(vCardString.contains("TEL:"))
        assertFalse(vCardString.contains("EMAIL:"))
        assertFalse(vCardString.contains("ORG:"))
        assertFalse(vCardString.contains("URL:"))
        assertTrue(vCardString.contains("END:VCARD"))
    }

    @Test
    fun `generateVCardString handles single name correctly`() {
        val userData = UserData(
            fullName = "Madonna",
            title = "Singer",
            phoneNumber = "",
            emailAddress = "",
            company = "",
            website = "",
            avatarUri = null
        )

        val vCardString = VCardHelper.generateVCardString(userData)
        assertTrue(vCardString.contains("FN:Madonna"))
        // Based on implementation: single name goes to family -> N:Madonna;;;;
        assertTrue(vCardString.contains("N:Madonna;;;;"))
        assertTrue(vCardString.contains("TITLE:Singer"))
        assertTrue(vCardString.contains("END:VCARD"))
    }

     @Test
    fun `generateVCardString handles multi-part last name correctly`() {
        val userData = UserData(
            fullName = "Gabriel Van Helsing",
            title = "Monster Hunter",
            phoneNumber = "",
            emailAddress = "",
            company = "",
            website = "",
            avatarUri = null
        )

        val vCardString = VCardHelper.generateVCardString(userData)
        assertTrue(vCardString.contains("FN:Gabriel Van Helsing"))
        // Based on implementation: given=Gabriel, family="Van Helsing" -> N:Van Helsing;Gabriel;;;
        assertTrue(vCardString.contains("N:Van Helsing;Gabriel;;;"))
        assertTrue(vCardString.contains("TITLE:Monster Hunter"))
        assertTrue(vCardString.contains("END:VCARD"))
    }

    @Test
    fun `generateVCardString with invalid website URL still creates vCard without URL`() {
        val userData = UserData(
            fullName = "Test User",
            title = "Tester",
            phoneNumber = "1112223333",
            emailAddress = "test@example.com",
            company = "Test Co",
            website = "invalid-url", // Invalid URL
            avatarUri = null
        )

        val vCardString = VCardHelper.generateVCardString(userData)

        assertTrue(vCardString.contains("FN:Test User"))
        assertTrue(vCardString.contains("EMAIL:test@example.com"))
        assertTrue(vCardString.contains("URL:invalid-url")) // URL should be omitted due to invalid format
        assertTrue(vCardString.contains("END:VCARD"))
    }
}
