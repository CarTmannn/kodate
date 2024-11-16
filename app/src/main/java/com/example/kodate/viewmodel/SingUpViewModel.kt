package com.example.kodate.viewmodel
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kodate.data.model.SignUpState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class SignUpViewModel : ViewModel() {
    private var _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> get() = _signUpState
    val db = FirebaseFirestore.getInstance()
    var text = ""
    private var _tempEmail = MutableStateFlow("")
    val tempEmail: StateFlow<String> get() = _tempEmail

    fun setTempEmail(email: String) {
        _tempEmail.value = email
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _signUpState.value = SignUpState(isLoading = true)
            try {
                val auth = FirebaseAuth.getInstance()
                val emailExists = checkIfEmailExists(email = email)
                if (!emailExists){
                    val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                    val uid = authResult.user?.uid

                    if(uid != null){
                        addEmailToUsersCollection(email = email, uid = uid)
                        _signUpState.value = SignUpState(isEmailSuccess = true)
                    }
                } else {
                    _signUpState.value = SignUpState(isEmailExists = true)
                    println("Email already exists")
                }
            } catch(e: Exception) {
                _signUpState.value = SignUpState(errorMessage = e.message)
            }
        }

    }

    suspend fun addEmailToUsersCollection(email: String, uid: String): Result<Boolean> {
        return try {
            val data = hashMapOf("email" to email)
            db.collection("user").document(uid).set(data).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun  addAnotherDataToUsersCollection(email: String, profilePic: Uri, name: String, bio: String, age: Int, gender:String){
        val userData = hashMapOf(
            "email" to email,
            "profilePic" to "$email",
            "name" to name,
            "bio" to bio,
            "age" to age,
            "gender" to gender
        )

        _signUpState.value = SignUpState(isLoading = true)

        try {
            val querySnapshot = db.collection("user").whereEqualTo("email", email).get().await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()

                uploadProfileImage(profilePic, email, typeImage = "profileImage")

                document.reference.set(userData).await()
                _signUpState.value = SignUpState(isSuccess = true)
                Log.d("Firestore", "Success updating the data")
            } else {
                Log.d("Firestore", "Failed to get the document with email: $email")
            }
        } catch (e: Exception) {
            Log.d("Firestore", "Error: ${e.message}")
        }
    }

    suspend fun uploadInterests(interests: List<String>, email: String, displayPic: Uri, city: String, likedUsers: List<String>, matchedUsers: List<String>){

        val userData: MutableMap<String, Any> = hashMapOf(
            "interests" to interests,
            "displayPic" to "$email",
            "city" to city,
            "likedUsers" to likedUsers,
            "matchedUsers" to matchedUsers)

        _signUpState.value = SignUpState(isLoading = true)
        try {
            val querySnapshot = db.collection("user").whereEqualTo("email", email).get().await()

            if (!querySnapshot.isEmpty){
                val document = querySnapshot.documents.first()
                uploadProfileImage(uri = displayPic, email = email, typeImage = "displayPict")
                document.reference.update(userData).await()

                _signUpState.value = SignUpState(isUploadImageSuccess = true)
                Log.d("Firestore", "Success updating the data")
            } else {
                Log.d("Firestore", "Failed updating the data")
            }
        } catch (e: Exception){
            Log.d("Firestore", "Failed to get the document with email: $email")
        }

    }

    suspend fun uploadProfileImage( uri: Uri, email: String, typeImage: String){
        val storage = FirebaseStorage.getInstance().reference;
        val image = storage.child("${typeImage}/${email}.jpg");
            try {
                image.putFile(uri).await()
                Log.d("Storage", "Image uploaded successfully")
            } catch (e: Exception) {
                Log.d("Storage", "Image upload failed: ${e.message}")
                _signUpState.value = SignUpState(isLoading = false)
            }

    }

    suspend fun checkIfEmailExists(email: String): Boolean{
        return try{
            val querySnapshot = db.collection("user").whereEqualTo("email", email).limit(1).get().await()
            !querySnapshot.isEmpty
        } catch (e: Exception){
            false
        }
    }

}