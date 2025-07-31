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
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
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
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BusinessCardViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: BusinessCardViewModel
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(VCardHelper)
        
        userRepository = mockk()
        viewModel = BusinessCardViewModel(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkObject(VCardHelper)
    }

    @Test
    fun `loadInitialData_whenRepositoryReturnsData_updatesStateAndCallsQrCodeGeneration`() = runTest {
        // Given
        val userData = UserData("Test User", "Test Title", "123456789", "test@example.com", "Test Company", "www.example.com")
        val dummyVCardString = "BEGIN:VCARD..."
        val mockAndroidBitmap = mockk<android.graphics.Bitmap>(relaxed = true)

        coEvery { userRepository.loadUserData() } returns userData
        coEvery { VCardHelper.generateVCardString(userData) } returns dummyVCardString
        coEvery { VCardHelper.generateQRCodeBitmap(dummyVCardString) } returns mockAndroidBitmap

        // When
        viewModel.loadInitialData()
        testDispatcher.scheduler.advanceUntilIdle() // Ensure all coroutines complete

        // Then - Focus on data loading, not bitmap conversion
        assertEquals(userData, viewModel.currentData.value)

        coVerify(exactly = 1) { userRepository.loadUserData() }
        coVerify(exactly = 1) { VCardHelper.generateVCardString(userData) }
        coVerify(exactly = 1) { VCardHelper.generateQRCodeBitmap(dummyVCardString) }
    }

    @Test
    fun `loadInitialData_whenRepositoryReturnsNull_updatesStateToNullAndSkipsQrCodeGeneration`() = runTest {
        // Given
        coEvery { userRepository.loadUserData() } returns null

        // When
        viewModel.loadInitialData()
        testDispatcher.scheduler.advanceUntilIdle() // Ensure all coroutines complete

        // Then
        assertNull(viewModel.currentData.value)

        coVerify(exactly = 1) { userRepository.loadUserData() }
        coVerify(exactly = 0) { VCardHelper.generateVCardString(any()) }
        coVerify(exactly = 0) { VCardHelper.generateQRCodeBitmap(any()) }
    }

    @Test
    fun `generateQrCode_whenSuccessful_callsHelperMethods`() = runTest {
        // Given
        val userData = UserData("Test User", "Test Title", "123456789", "test@example.com", "Test Company", "www.example.com")
        val vCardString = "BEGIN:VCARD\nVERSION:3.0\nN:User;Test\nFN:Test User\nORG:Test Company\nTITLE:Test Title\nTEL;TYPE=WORK,VOICE:123456789\nEMAIL:test@example.com\nURL:www.example.com\nEND:VCARD" // More realistic VCard
        val mockBitmap = mockk<android.graphics.Bitmap>(relaxed = true)

        coEvery { VCardHelper.generateVCardString(userData) } returns vCardString
        coEvery { VCardHelper.generateQRCodeBitmap(vCardString) } returns mockBitmap

        // When
        viewModel.generateQrCode(userData)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Focus on the helper method calls, not bitmap conversion
        coVerify(exactly = 1) { VCardHelper.generateVCardString(userData) }
        coVerify(exactly = 1) { VCardHelper.generateQRCodeBitmap(vCardString) }
    }

    @Test
    fun `generateQrCode_whenBitmapGenerationReturnsNull_handlesGracefully`() = runTest {
        // Given
        val userData = UserData("Test User", "Test Title", "123456789", "test@example.com", "Test Company", "www.example.com")
        val vCardString = "BEGIN:VCARD..." // Dummy vCard string
        coEvery { VCardHelper.generateVCardString(userData) } returns vCardString
        coEvery { VCardHelper.generateQRCodeBitmap(vCardString) } returns null

        // When
        viewModel.generateQrCode(userData)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { VCardHelper.generateVCardString(userData) }
        coVerify(exactly = 1) { VCardHelper.generateQRCodeBitmap(vCardString) }
    }

    @Test
    fun `generateQrCode_whenVCardStringGenerationThrowsException_handlesGracefully`() = runTest {
        // Given
        val userData = UserData("Test User", "Test Title", "123456789", "test@example.com", "Test Company", "www.example.com")
        val exception = RuntimeException("VCard String generation failed")
        coEvery { VCardHelper.generateVCardString(userData) } throws exception

        // When
        viewModel.generateQrCode(userData)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { VCardHelper.generateVCardString(userData) }
        coVerify(exactly = 0) { VCardHelper.generateQRCodeBitmap(any()) } // Should not be called
    }

    @Test
    fun `generateQrCode_whenBitmapGenerationThrowsException_handlesGracefully`() = runTest {
        // Arrange
        val userData = UserData("Test User", "Test Title", "123456789", "test@example.com", "Test Company", "www.example.com")
        val vCardString = "BEGIN:VCARD..." // Dummy vCard string
        val exception = RuntimeException("Bitmap generation failed")

        coEvery { VCardHelper.generateVCardString(userData) } returns vCardString
        coEvery { VCardHelper.generateQRCodeBitmap(vCardString) } throws exception

        // Act
        viewModel.generateQrCode(userData)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify(exactly = 1) { VCardHelper.generateVCardString(userData) }
        coVerify(exactly = 1) { VCardHelper.generateQRCodeBitmap(vCardString) }
    }
}
