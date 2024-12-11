package com.example.kodate.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kodate.data.model.TextFieldState
import com.example.kodate.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LogInViewModel(context: Context) : ViewModel() {
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

    fun setUserState(email: String){
        _fetchUserState.value = _fetchUserState.value!!.copy(email = email)
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

    val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_EMAIL = "email"
    }

    init {
        _isLoggedin.value = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        val email = sharedPreferences.getString(KEY_EMAIL, null)
        if (email != null) {
            fetchUserDataByEmail(email)
        } else {
            println("No email found in SharedPreferences")
        }
    }


    suspend fun logIn(email: String, password: String) {
        _isLoading.value = true
        try {
            val authResult = db.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid
            if(uid != null){
                getDb.collection("user").document(uid).addSnapshotListener { snaphot, e ->
                    if (e != null) {
                        println("Failed to listen to document: ${e.message}")
                        _isLoggedin.value = false
                        return@addSnapshotListener
                    }

                    if (snaphot != null && snaphot.exists()) {
                        val profile = snaphot.toObject(User::class.java) ?: User()
                        _fetchUserState.value = profile
                        _isLoggedin.value = true

                        sharedPreferences.edit().apply {
                            putBoolean(KEY_IS_LOGGED_IN, true)
                            putString(KEY_EMAIL, profile.email)
                            apply()
                        }

                    } else {
                        println("Document does not exist")
                        _isLoggedin.value = false
                        _isLoading.value = false
                    }
                }
            }
        } catch (e: Exception) {
            _isLoggedin.value = false
            println("Failed to login: ${e.message}")
            _isLoading.value = false
        } finally {
            _isLoading.value = false
        }
    }

    private fun fetchUserDataByEmail(email: String) {
        getDb.collection("user").whereEqualTo("email", email)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Error fetching user data: ${error.message}")
                    _fetchUserState.value = null
                    _isLoggedin.value = false
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val profile = snapshot.documents[0].toObject(User::class.java)?.copy(userId = snapshot.documents[0].id)
                    _fetchUserState.value = profile
                    _isLoggedin.value = true
                } else {
                    _fetchUserState.value = null
                    _isLoggedin.value = false
                }
            }
    }

    fun logout() {
        db.signOut()
        _isLoggedin.value = false
        sharedPreferences.edit().clear().apply()
    }

}
