package com.example.kodate.viewmodel

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.kodate.data.model.TextFieldState
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData

class TextFieldViewModel : ViewModel() {
    var textFieldState = mutableStateOf(TextFieldState())
        private set

    private val db = FirebaseFirestore.getInstance()
    private var _interest = MutableStateFlow(TextFieldState())
    val interests: StateFlow<TextFieldState> = _interest
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri.asStateFlow()
    private val _imageProfileUri = MutableStateFlow<Uri?>(null)
    val imageProfileUri: StateFlow<Uri?> = _imageProfileUri.asStateFlow()

    fun updateImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun updateImageProfileUri(uri: Uri?){
        _imageProfileUri.value = uri
    }



    init {
        fetchInterests()
    }

    fun clearTextField(){
        textFieldState.value = TextFieldState()
    }

    fun setEmail(email: String){
        textFieldState.value = textFieldState.value.copy(email = email)
        validateEmail()
    }

    fun setPassword(password: String){
        textFieldState.value = textFieldState.value.copy(password = password)
        validatePassword()
    }

    fun setConfirmPassword(confirmPassword: String){
        textFieldState.value = textFieldState.value.copy(confirmPassword = confirmPassword)
        validateConfirmPassword()
    }

    fun setName(name: String){
        textFieldState.value = textFieldState.value.copy(name = name)
    }

    fun setBio(bio: String){
        textFieldState.value = textFieldState.value.copy(bio = bio)
    }

    fun addAge(){
        textFieldState.value = textFieldState.value.copy(age = textFieldState.value.age + 1)
    }

    fun setCity(city: String){
        textFieldState.value = textFieldState.value.copy(city = city)
    }

    fun reduceAge(){
        textFieldState.value = textFieldState.value.copy(age = textFieldState.value.age - 1)
    }

    fun toMale(){
        textFieldState.value = textFieldState.value.copy(isMale = true)
    }

    fun toFemale(){
        textFieldState.value = textFieldState.value.copy(isMale = false)
    }

    fun addItemToInterestList(item:String){

        val updateList = textFieldState.value.interests.toMutableList()

        if (updateList.contains(item) || textFieldState.value.interests.size == 3){
            println("item already exists")
        } else {
             updateList.add(item)
        }

        textFieldState.value = textFieldState.value.copy(interests = updateList)
    }

    fun removeItemFromInterestList(interest: String){
        val updateList = textFieldState.value.interests.toMutableList()

        if (updateList.remove(interest)){
            textFieldState.value = textFieldState.value.copy(interests = updateList)
        } else {
            println("failed to remove interest")
        }
    }

    fun setChatMessage(chatMessage: String) {
        textFieldState.value = textFieldState.value.copy(chatMessage = chatMessage)
    }

 fun validateEmail(){
        val email = textFieldState.value.email.trim()
        textFieldState.value = textFieldState.value.copy(
            emailError = if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                "Invalid email error"
            } else {
                ""
            }
        )
    }

 fun validatePassword(){
        val password = textFieldState.value.password
        textFieldState.value = textFieldState.value.copy(
            passwordError = if (password.length < 8){
                "Password must be at least 8 characters"
            } else {
                ""
            }
        )
    }

 fun validateConfirmPassword(){
        val confirmPassword = textFieldState.value.confirmPassword
        textFieldState.value = textFieldState.value.copy(
            confirmPasswordError = if (confirmPassword != textFieldState.value.password){
                "password does not match"
            } else {
                ""
            }
        )
    }

    fun validateAllFields(): Boolean {
        validateEmail()
        validatePassword()
        validateConfirmPassword()


        val isEmailValid = textFieldState.value.emailError.isEmpty()
        val isPasswordValid = textFieldState.value.passwordError.isEmpty()
        val isConfirmPasswordValid = textFieldState.value.confirmPasswordError.isEmpty()

        return isEmailValid && isPasswordValid && isConfirmPasswordValid
    }

    private fun fetchInterests(){
        db.collection("assets").document("interests").addSnapshotListener { snapshot, error ->
            if (error != null){
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()){
                val interests = snapshot.get("list_of_interests") as List<String> ?: emptyList()
                _interest.value = TextFieldState(interests = interests)
            }
        }
    }

}