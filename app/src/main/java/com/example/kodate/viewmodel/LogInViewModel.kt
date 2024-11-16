package com.example.kodate.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kodate.data.model.TextFieldState
import com.example.kodate.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LogInViewModel : ViewModel() {
    var loginState = mutableStateOf(TextFieldState())
        private set
    private var _fetchUserState = MutableStateFlow<User?>(null)
    val fetchUserState: StateFlow<User?> get() = _fetchUserState
    val db = FirebaseAuth.getInstance()
    val getDb = FirebaseFirestore.getInstance()
    private var _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    private var _isLoggedin = MutableStateFlow(false)
    val isLoggedin: StateFlow<Boolean> get() = _isLoggedin


    fun setEmail(email: String){
        loginState.value = loginState.value.copy(email = email)
        validateEmail()
    }

    fun setPassword(password: String){
        loginState.value = loginState.value.copy(password = password)
        validatePassword()
    }

    fun validateEmail(){
        val email = loginState.value.email.trim()
        loginState.value = loginState.value.copy(
            emailError = if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                "Invalid email error"
            } else {
                ""
            }
        )
    }

    fun validatePassword(){
        val password = loginState.value.password
        loginState.value = loginState.value.copy(
            passwordError = if (password.length < 8){
                "Password must be at least 8 characters"
            } else {
                ""
            }
        )
    }

    suspend fun logIn(email: String, password: String) {
        _isLoading.value = true
        try {
            val authResult = db.signInWithEmailAndPassword(email, password).await()
            val uid  = authResult.user?.uid
            if (uid != null){
                val doc = getDb.collection("user").document(uid).get().await()
                if (doc.exists()){
                    val profile = doc.toObject(User::class.java)?.copy(userId = doc.id)
                    _fetchUserState.value = profile

                } else {
                    println("document does not exists")
                    _fetchUserState.value = null
                }
            }
            _isLoggedin.value = true
        } catch (e: Exception) {
            _isLoggedin.value = false
            println("Failed to login: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }
}
