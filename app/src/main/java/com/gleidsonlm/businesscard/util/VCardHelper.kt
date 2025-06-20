package com.gleidsonlm.businesscard.util

import android.graphics.Bitmap
import android.util.Log
import com.gleidsonlm.businesscard.ui.UserData
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import ezvcard.Ezvcard
import ezvcard.VCard
import ezvcard.property.Email
import ezvcard.property.Organization
import ezvcard.property.StructuredName
import ezvcard.property.Telephone
import ezvcard.property.Url

object VCardHelper {

    fun generateVCardString(userData: UserData): String {
        val vcard = VCard()

        // Formatted Name (FN)
        if (userData.fullName.isNotBlank()) {
            vcard.setFormattedName(userData.fullName)
        }

        // Structured Name (N)
        if (userData.fullName.isNotBlank()) {
            val n = StructuredName()
            val nameParts = userData.fullName.trim().split(" ", limit = 2)
            n.given = nameParts.getOrNull(0)
            if (nameParts.size > 1) {
                n.family = nameParts.getOrNull(1)
            }
            vcard.structuredName = n
        }

        // Title
        if (userData.title.isNotBlank()) {
            vcard.addTitle(userData.title)
        }

        // Phone
        if (userData.phoneNumber.isNotBlank()) {
            vcard.addTelephoneNumber(Telephone(userData.phoneNumber))
        }

        // Email
        if (userData.emailAddress.isNotBlank()) {
            val email = Email(userData.emailAddress) // Create Email property
            vcard.addEmail(email)
        }

        // Organization (Company)
        if (userData.company.isNotBlank()) {
            val organization = Organization() // Create Organization property
            organization.getValues().add(userData.company)
            vcard.organization = organization
        }

        // Website URL
        if (userData.website.isNotBlank()) {
            try {
                val websiteUrl = Url(userData.website)
                vcard.addUrl(websiteUrl)
            } catch (e: IllegalArgumentException) {
                Log.e("VCardHelper", "Invalid URL format for website: ${userData.website}", e)
            }
        }

        return Ezvcard.write(vcard).go()
    }

    fun generateQRCodeBitmap(vCardString: String, size: Int = 512): Bitmap? {
        val barcodeEncoder = BarcodeEncoder()
        return try {
            barcodeEncoder.encodeBitmap(vCardString, BarcodeFormat.QR_CODE, size, size)
        } catch (e: WriterException) {
            Log.e("VCardHelper", "Failed to generate QR Code Bitmap", e)
            null
        } catch (e: Exception) {
            // Catch any other unexpected errors during bitmap generation
            Log.e("VCardHelper", "Unexpected error generating QR Code Bitmap", e)
            null
        }
    }
}
