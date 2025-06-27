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
import ezvcard.parameter.TelephoneType

/**
 * Utility object for creating VCard strings from [UserData] and
 * generating corresponding QR Code bitmap images.
 *
 * This object provides methods to:
 * 1. Convert user information into a standard VCard format string.
 * 2. Generate a QR code [Bitmap] from a VCard string.
 */
object VCardHelper {

    /**
     * Generates a VCard string from user data.
     *
     * @param userData The user data to include in the VCard.
     * @return The VCard string.
     */
    fun generateVCardString(userData: UserData): String {
        val vcard = VCard()

        setFullNameAndStructure(vcard, userData.fullName)
        setTitle(vcard, userData.title)
        setPhoneNumber(vcard, userData.phoneNumber)
        setEmail(vcard, userData.emailAddress)
        setOrganization(vcard, userData.company)
        setWebsiteUrl(vcard, userData.website)

        return Ezvcard.write(vcard).go()
    }

    /**
     * Sets the formatted name and structured name for the VCard.
     *
     * @param vcard The VCard object.
     * @param fullName The full name of the user.
     */
    private fun setFullNameAndStructure(vcard: VCard, fullName: String) {
        if (fullName.isNotBlank()) {
            vcard.setFormattedName(fullName) // FN field remains the full name

            val structuredName = StructuredName()
            val nameParts = fullName.trim().split(" ", limit = 2)

            if (nameParts.isNotEmpty()) {
                if (nameParts.size == 1) {
                    // For a single name (e.g., "Madonna"), set it as the family name.
                    // This aligns with the test expectation N:Madonna;;;;
                    structuredName.family = nameParts[0]
                    // structuredName.given is null by default
                } else { // nameParts.size > 1
                    // For "John Doe", nameParts[0] is "John" (Given), nameParts[1] is "Doe" (Family)
                    // To get N:Doe;John;;;, ez-vcard's family property should be "Doe" and given "John".
                    structuredName.given = nameParts[0]
                    structuredName.family = nameParts[1]
                }
                vcard.structuredName = structuredName
            }
        }
    }

    /**
     * Sets the title for the VCard.
     *
     * @param vcard The VCard object.
     * @param title The title of the user.
     */
    private fun setTitle(vcard: VCard, title: String) {
        if (title.isNotBlank()) {
            vcard.addTitle(title)
        }
    }

    /**
     * Sets the phone number for the VCard.
     *
     * @param vcard The VCard object.
     * @param phoneNumber The phone number of the user.
     */
    private fun setPhoneNumber(vcard: VCard, phoneNumber: String) {
        if (phoneNumber.isNotBlank()) {
            val tel = Telephone(phoneNumber)
            tel.types.add(TelephoneType.VOICE)
            vcard.addTelephoneNumber(tel)
        }
    }

    /**
     * Sets the email address for the VCard.
     *
     * @param vcard The VCard object.
     * @param emailAddress The email address of the user.
     */
    private fun setEmail(vcard: VCard, emailAddress: String) {
        if (emailAddress.isNotBlank()) {
            val email = Email(emailAddress)
            vcard.addEmail(email)
        }
    }

    /**
     * Sets the organization for the VCard.
     *
     * @param vcard The VCard object.
     * @param company The company of the user.
     */
    private fun setOrganization(vcard: VCard, company: String) {
        if (company.isNotBlank()) {
            val organization = Organization()
            organization.getValues().add(company)
            vcard.organization = organization
        }
    }

    /**
     * Sets the website URL for the VCard.
     *
     * @param vcard The VCard object.
     * @param website The website URL of the user.
     */
    private fun setWebsiteUrl(vcard: VCard, website: String) {
        if (website.isNotBlank()) {
            try {
                val websiteUrl = Url(website)
                vcard.addUrl(websiteUrl)
            } catch (e: IllegalArgumentException) {
                Log.e("VCardHelper", "Invalid URL format for website: $website", e)
            }
        }
    }

    /**
     * Generates a QR code bitmap from a VCard string.
     *
     * This method takes a VCard string, encodes it into a QR code,
     * and returns it as a Bitmap image. It handles potential errors during
     * the encoding process by logging them and returning null.
     *
     * @param vCardString The VCard data string to encode into the QR code.
     * @param size The desired width and height of the QR code bitmap in pixels.
     *             Defaults to 512 pixels if not specified.
     * @return A [Bitmap] object representing the QR code, or `null` if an error occurs
     *         during generation (e.g., due to `WriterException` or other exceptions).
     *         The method logs these errors to LogCat.
     */
    fun generateQRCodeBitmap(vCardString: String, size: Int = 512): Bitmap? {
        val barcodeEncoder = BarcodeEncoder()
        return try {
            // Encode the VCard string into a QR code bitmap
            barcodeEncoder.encodeBitmap(vCardString, BarcodeFormat.QR_CODE, size, size)
        } catch (e: WriterException) {
            // Log an error if QR code generation fails due to a WriterException
            Log.e("VCardHelper", "Failed to generate QR Code Bitmap due to WriterException.", e)
            null // Return null indicating failure
        } catch (e: Exception) {
            // Log an error for any other unexpected issues during QR code generation
            Log.e("VCardHelper", "Unexpected error generating QR Code Bitmap.", e)
            null // Return null indicating failure
        }
    }
}
