package com.gleidsonlm.businesscard.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.gleidsonlm.businesscard.data.repository.UserRepository
import com.gleidsonlm.businesscard.ui.UserData
import com.gleidsonlm.businesscard.util.VCardHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class BusinessCardViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: BusinessCardViewModel
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        userRepository = mockk()
        // Mock VCardHelper object before it's used by the ViewModel during QR code generation
        mockkObject(VCardHelper)
        viewModel = BusinessCardViewModel(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadInitialData_whenRepositoryReturnsData_updatesStateAndGeneratesQrCode`() = runTest {
        // Given
        val userData = UserData("Test User", "Test Title", "123456789", "test@example.com", "Test Company", "www.example.com", "Test Address")
        val dummyVCardString = "BEGIN:VCARD..."
        val mockAndroidBitmap = mockk<android.graphics.Bitmap>(relaxed = true)
        val mockComposeImageBitmap = mockk<ImageBitmap>(relaxed = true)

        coEvery { userRepository.loadUserData() } returns userData
        coEvery { VCardHelper.generateVCardString(userData) } returns dummyVCardString
        coEvery { VCardHelper.generateQRCodeBitmap(dummyVCardString) } returns mockAndroidBitmap
        every { mockAndroidBitmap.asImageBitmap() } returns mockComposeImageBitmap

        // When
        viewModel.loadInitialData()
        testDispatcher.scheduler.advanceUntilIdle() // Ensure all coroutines complete

        // Then
        assertEquals(userData, viewModel.currentData.value)
        assertNotNull(viewModel.qrCodeImageBitmap.value)
        assertEquals(mockComposeImageBitmap, viewModel.qrCodeImageBitmap.value)

        coVerify(exactly = 1) { userRepository.loadUserData() }
        coVerify(exactly = 1) { VCardHelper.generateVCardString(userData) }
        coVerify(exactly = 1) { VCardHelper.generateQRCodeBitmap(dummyVCardString) }
        verify(exactly = 1) { mockAndroidBitmap.asImageBitmap() }
    }

    @Test
    fun `loadInitialData_whenRepositoryReturnsNull_updatesStateToNullAndQrCodeToNull`() = runTest {
        // Given
        coEvery { userRepository.loadUserData() } returns null

        // When
        viewModel.loadInitialData()
        testDispatcher.scheduler.advanceUntilIdle() // Ensure all coroutines complete

        // Then
        assertNull(viewModel.currentData.value)
        assertNull(viewModel.qrCodeImageBitmap.value)

        coVerify(exactly = 1) { userRepository.loadUserData() }
        coVerify(exactly = 0) { VCardHelper.generateVCardString(any()) }
        coVerify(exactly = 0) { VCardHelper.generateQRCodeBitmap(any()) }
    }

    @Test
    fun `generateQrCode_whenSuccessful_updatesQrCodeImageBitmap`() = runTest {
        // Given
        val userData = UserData("Test User", "Test Title", "123456789", "test@example.com", "Test Company", "www.example.com", "Test Address")
        val vCardString = "BEGIN:VCARD\nVERSION:3.0\nN:User;Test\nFN:Test User\nORG:Test Company\nTITLE:Test Title\nTEL;TYPE=WORK,VOICE:123456789\nEMAIL:test@example.com\nURL:www.example.com\nADR;TYPE=WORK:;;Test Address\nEND:VCARD" // More realistic VCard
        val mockBitmap = mockk<android.graphics.Bitmap>(relaxed = true)
        val mockImageBitmap = mockk<androidx.compose.ui.graphics.ImageBitmap>(relaxed = true)

        coEvery { VCardHelper.generateVCardString(userData) } returns vCardString
        coEvery { VCardHelper.generateQRCodeBitmap(vCardString) } returns mockBitmap
        // Manually mock the extension function as ImageBitmap
        every { mockBitmap.asImageBitmap() } returns mockImageBitmap

        // When
        viewModel.generateQrCode(userData)
        testDispatcher.scheduler.advanceUntilIdle()


        // Then
        assertEquals(mockImageBitmap, viewModel.qrCodeImageBitmap.value)
        coVerify(exactly = 1) { VCardHelper.generateVCardString(userData) }
        coVerify(exactly = 1) { VCardHelper.generateQRCodeBitmap(vCardString) }
        verify(exactly = 1) { mockBitmap.asImageBitmap() } // Verify the conversion call
    }

    @Test
    fun `generateQrCode_whenBitmapGenerationReturnsNull_setsQrCodeImageBitmapToNull`() = runTest {
        // Given
        val userData = UserData("Test User", "Test Title", "123456789", "test@example.com", "Test Company", "www.example.com", "Test Address")
        val vCardString = "BEGIN:VCARD..." // Dummy vCard string
        coEvery { VCardHelper.generateVCardString(userData) } returns vCardString
        coEvery { VCardHelper.generateQRCodeBitmap(vCardString) } returns null

        // When
        viewModel.generateQrCode(userData)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertNull(viewModel.qrCodeImageBitmap.value)
        coVerify(exactly = 1) { VCardHelper.generateVCardString(userData) }
        coVerify(exactly = 1) { VCardHelper.generateQRCodeBitmap(vCardString) }
    }

    @Test
    fun `generateQrCode_whenVCardStringGenerationThrowsException_setsQrCodeImageBitmapToNull`() = runTest {
        // Given
        val userData = UserData("Test User", "Test Title", "123456789", "test@example.com", "Test Company", "www.example.com", "Test Address")
        val exception = RuntimeException("VCard String generation failed")
        coEvery { VCardHelper.generateVCardString(userData) } throws exception

        // When
        viewModel.generateQrCode(userData)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertNull(viewModel.qrCodeImageBitmap.value)
        coVerify(exactly = 1) { VCardHelper.generateVCardString(userData) }
        coVerify(exactly = 0) { VCardHelper.generateQRCodeBitmap(any()) } // Should not be called
    }

    @Test
    fun `generateQrCode_whenBitmapGenerationThrowsException_setsQrCodeImageBitmapToNull`() = runTest {
        // Arrange
        val userData = UserData("Test User", "Test Title", "123456789", "test@example.com", "Test Company", "www.example.com", "Test Address")
        val vCardString = "BEGIN:VCARD..." // Dummy vCard string
        val exception = RuntimeException("Bitmap generation failed")

        coEvery { VCardHelper.generateVCardString(userData) } returns vCardString
        coEvery { VCardHelper.generateQRCodeBitmap(vCardString) } throws exception

        // Act
        viewModel.generateQrCode(userData)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertNull(viewModel.qrCodeImageBitmap.value)
        coVerify(exactly = 1) { VCardHelper.generateVCardString(userData) }
        coVerify(exactly = 1) { VCardHelper.generateQRCodeBitmap(vCardString) }
    }
}
