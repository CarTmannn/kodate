package com.example.kodate.viewmodel

import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.kodate.data.model.LastChat
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class MessagesViewModel : ViewModel() {
    val db = FirebaseFirestore.getInstance()
    private var _listChats = MutableStateFlow<List<LastChat>>(emptyList())
    val listChats: StateFlow<List<LastChat>> = _listChats
    private var _tempEmail = MutableStateFlow<String>("")
    val tempEmail: StateFlow<String> get() = _tempEmail
    private var _tempchatId = MutableStateFlow<String>("")
    val tempchatId: StateFlow<String> get() = _tempchatId

    fun setTempEmail(email: String){
        _tempEmail.value = email
    }

    fun setTempChatId(chatId: String){
        _tempchatId.value = chatId
    }


    suspend fun initiateChat(user1: String, user2: String){
        val chatId = "${minOf(user1, user2)}_${maxOf(user1, user2)}"
        val chatRef = db.collection("chat").document(chatId)
        val chatSnapshot = chatRef.get().await()
        val chatData = hashMapOf(
            "users" to listOf(user1, user2),
            "lastMessage" to "",
            "lastMessageAt" to null,
        )

        if(!chatSnapshot.exists()){
            chatRef.set(chatData).await()
        }
    }

    fun generateChatId(user1: String, user2: String): String {
        val chatId = "${minOf(user1, user2)}_${maxOf(user1, user2)}"
        return chatId
    }

    suspend fun sendMessage(chatId: String, senderId: String, message: String) {
        val chatRef = db.collection("chat").document(chatId)
        val messageRef = chatRef.collection("message")

        val receiverId = getReceiverIdFromChatId(chatId, senderId)

        val newMessage = hashMapOf(
            "senderId" to senderId,
            "message" to message,
            "setAt" to Timestamp.now(),
            "isRead" to false
        )

        messageRef.add(newMessage).await()

        chatRef.update(
            mapOf(
                "lastMessage" to message,
                "lastMessageAt" to Timestamp.now(),
                "read" to mapOf(
                    senderId to true,
                    receiverId to false
                )
            )
        ).await()
    }


    fun getReceiverIdFromChatId(chatId: String, senderId: String): String {
        val ids = chatId.split("_")
        if (ids.size == 2) {
            return if (chatId.startsWith(senderId)) ids[1] else ids[0]
        } else {
            throw IllegalArgumentException("chatId format is invalid. Expected format: senderId_receiverId")
        }
    }

    fun getChat(email: String) {
        val chatRef = db.collection("chat")
        chatRef.whereArrayContains("users", email)
            .orderBy("lastMessageAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Chat", "Error getting documents: ", error)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val lastChats = snapshot.documents.mapNotNull { document ->
                        Log.d("Chat", "Document data: ${document.data}")
                        val lastChat = document.toObject(LastChat::class.java)
                        lastChat?.copy(chatId = document.id)
                    }
                    _listChats.value = lastChats
                }
            }
    }


    suspend fun fetchUserNameByEmail(email: String): String {
        return try {
            val userRef = db.collection("user").whereEqualTo("email", email).get().await()
            if (!userRef.isEmpty) {
                val userDoc = userRef.documents[0]
                userDoc.getString("name") ?: "Unknown"
            } else {
                "Unknown"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Unknown"
        }
    }

    fun formatTimestamp(timestamp: Date): String {
        val zonedDateTime = timestamp.toInstant().atZone(ZoneId.systemDefault())
        return zonedDateTime.format(DateTimeFormatter.ofPattern("HH.mm"))
    }
}