package com.example.kodate.viewmodel

import androidx.compose.runtime.setValue
import androidx.core.app.GrammaticalInflectionManagerCompat.GrammaticalGender
import androidx.lifecycle.ViewModel
import com.example.kodate.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

class HomeViewModel : ViewModel() {
    private var _fetchUsers = MutableStateFlow<List<User?>>(emptyList())
    val fetchUsers: StateFlow<List<User?>> get() = _fetchUsers
    val db = FirebaseFirestore.getInstance()

    private val clearedUsers = mutableSetOf<String>()

    fun getListUsers(email: String, gender: String, likedUsers: List<String>) {
        db.collection("user")
            .whereNotEqualTo("email", email)
            .whereEqualTo("gender", gender)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val userLists = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(User::class.java)
                    }.filterNot { user ->
                        likedUsers.contains(user.email) || clearedUsers.contains(user.email)
                    }
                    _fetchUsers.value = userLists
                } else {
                    _fetchUsers.value = emptyList()
                }
            }
            .addOnFailureListener { exception ->
                println("Error fetching users: ${exception.message}")
                _fetchUsers.value = emptyList()
            }
            .addOnCompleteListener {
            }
    }



    fun clearUser(email: String) {
        clearedUsers.add(email)
    }

    suspend fun handleLike(user1Email: String, user2Email: String) {
        val likeData = hashMapOf(
            "fromUserId" to user1Email,
            "toUserId" to user2Email,
            "createdAt" to Timestamp.now()
        )
        val snapshot = db.collection("user").whereEqualTo("email", user1Email).get().await()
        val snapshot2 = db.collection("user").whereEqualTo("email", user2Email).get().await()
        val likeRef = db.collection("likes").document("${user1Email}_to_${user2Email}")

        if (!snapshot.isEmpty){
            val doc = snapshot.documents.first
            doc.reference.update("likedUsers", FieldValue.arrayUnion(user2Email))
        } else {
            println("Failed add like")
        }

        likeRef.set(likeData)

        val reciprocalLikeRef = db.collection("likes").document("${user2Email}_to_${user1Email}")
        reciprocalLikeRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val matchData = mapOf(
                    "user1" to user1Email,
                    "user2" to user2Email,
                    "matchedAt" to Timestamp.now()
                )
                db.collection("matches").add(matchData).addOnSuccessListener {
                    if (!snapshot.isEmpty){
                        val doc = snapshot.documents.first
                        doc.reference.update("matchedUsers", FieldValue.arrayUnion(user2Email))
                    } else {
                        println("Failed add like")
                    }
                    if (!snapshot2.isEmpty){
                        val doc = snapshot2.documents.first
                        doc.reference.update("matchedUsers", FieldValue.arrayUnion(user1Email))
                    } else {
                        println("Failed add like")
                    }
                    likeRef.delete()
                    reciprocalLikeRef.delete()
                }

            }
        }
    }

}