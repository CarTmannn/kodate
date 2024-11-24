package com.example.kodate.viewmodel

import android.adservices.common.AdServicesOutcomeReceiver
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.kodate.data.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatRoomViewModel : ViewModel() {
    private var _listChats = MutableStateFlow<List<Message>>(emptyList())
    val listChats: StateFlow<List<Message>> get() = _listChats
    val db = FirebaseFirestore.getInstance()


    fun getTexts(user1: String, user2: String){
        val docId = "${minOf(user1, user2)}_${maxOf(user1, user2)}"
        val chatRef = db.collection("chat").document(docId).collection("message").orderBy("setAt", Query.Direction.ASCENDING)
        val chatSnapShot = chatRef.addSnapshotListener{ snapshot, error
        -> if (error != null){
            return@addSnapshotListener
        }
            if (snapshot != null && !snapshot.isEmpty){
                val listTexts = snapshot.documents.mapNotNull {
                    doc -> doc.toObject(Message::class.java)
                }
                _listChats.value = listTexts
            }
        }

    }

    fun updateReadStatus(chatId: String, email: String) {
        val chatRef = db.collection("chat").document(chatId)
        chatRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val readMap = document.get("read") as? MutableMap<String, Boolean> ?: mutableMapOf()

                    readMap[email] = true

                    chatRef.update("read", readMap)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Read status updated for $email.")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Failed to update read status for $email.", e)
                        }
                } else {
                    Log.e("Firestore", "Document does not exist")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to fetch document", e)
            }
    }



}