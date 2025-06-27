package com.gleidsonlm.businesscard.util

import com.gleidsonlm.businesscard.ui.UserData
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

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
        assertTrue(vCardString.contains("VERSION:4.0")) // ez-vcard defaults to 4.0
        assertTrue(vCardString.contains("FN:John Doe"))
        assertTrue(vCardString.contains("N:Doe;John;;;"))
        assertTrue(vCardString.contains("TITLE:Software Engineer"))
        assertTrue(vCardString.contains("TEL;TYPE=voice:1234567890")) // Type might vary based on library defaults
        assertTrue(vCardString.contains("EMAIL:john.doe@example.com"))
        assertTrue(vCardString.contains("ORG:Example Corp"))
        assertTrue(vCardString.contains("URL:https://example.com"))
        assertTrue(vCardString.contains("END:VCARD"))
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
        assertTrue(vCardString.contains("EMAIL:jane.doe@example.com"))
        assertFalse(vCardString.contains("TITLE:"))
        assertFalse(vCardString.contains("TEL:"))
        assertFalse(vCardString.contains("ORG:"))
        assertFalse(vCardString.contains("URL:"))
        assertTrue(vCardString.contains("END:VCARD"))
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
        assertTrue(vCardString.contains("N:Madonna;;;;")) // Given name, family name empty
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
