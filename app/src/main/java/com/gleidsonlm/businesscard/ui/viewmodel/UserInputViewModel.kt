package com.gleidsonlm.businesscard.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gleidsonlm.businesscard.ui.UserData
import com.gleidsonlm.businesscard.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserInputViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _emailAddress = MutableStateFlow("")
    val emailAddress: StateFlow<String> = _emailAddress

    private val _company = MutableStateFlow("")
    val company: StateFlow<String> = _company

    private val _website = MutableStateFlow("")
    val website: StateFlow<String> = _website

    private val _avatarUri = MutableStateFlow<String?>(null)
    val avatarUri: StateFlow<String?> = _avatarUri

    fun loadExistingData(data: UserData?) {
        data?.let {
            _fullName.value = it.fullName
            _title.value = it.title
            _phoneNumber.value = it.phoneNumber
            _emailAddress.value = it.emailAddress
            _company.value = it.company
            _website.value = it.website
            _avatarUri.value = it.avatarUri
        }
    }

    // This will be refined in Step 2.3 to be more type-safe
    fun onFieldChange(fieldName: String, value: String) {
        when (fieldName) {
            "fullName" -> _fullName.value = value
            "title" -> _title.value = value
            "phoneNumber" -> _phoneNumber.value = value
            "emailAddress" -> _emailAddress.value = value
            "company" -> _company.value = value
            "website" -> _website.value = value
        }
    }

    fun saveUserData() {
        viewModelScope.launch {
            val userDataToSave = UserData(
                fullName = _fullName.value,
                title = _title.value,
                phoneNumber = _phoneNumber.value,
                emailAddress = _emailAddress.value,
                company = _company.value,
                website = _website.value,
                avatarUri = _avatarUri.value
            )
            userRepository.saveUserData(userDataToSave)
        }
    }

    fun selectImageFromGalleryClicked() {
        // Will be implemented later
    }

    fun takePhotoClicked() {
        // Will be implemented later
    }

    fun fetchGravatarClicked(email: String) {
        // Will be implemented later
    }
}
