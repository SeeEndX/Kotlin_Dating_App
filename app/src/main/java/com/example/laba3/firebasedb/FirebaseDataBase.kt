package com.example.laba3.firebasedb

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

val response: MutableState<DataState> = mutableStateOf(DataState.Empty)
val database = Firebase.database
val messageRef = database.getReference("message")

@Composable
fun fetchDataFromFirebase() {
    val messages = mutableListOf<Messages>()
    response.value = DataState.Loading

    DisposableEffect(Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    val message = childSnapshot.getValue(Messages::class.java)
                    if (message != null){
                        messages.add(message)
                    }
                }
                response.value = DataState.Success(messages)
            }

            override fun onCancelled(error: DatabaseError) {
                response.value = DataState.Failure(error.message)
            }
        }

        messageRef.addValueEventListener(listener)

        onDispose {
            messageRef.removeEventListener(listener)
        }
    }
}

fun addMessageToFirebase(message: Messages) {
    val messageRef = messageRef.child(message.id.toString())
    messageRef.setValue(message)
}

fun getMessageCount(callback: (Long) -> Unit) {
    val messageRef = Firebase.database.getReference("message")
    messageRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val count = dataSnapshot.childrenCount
            callback(count)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Обработка ошибок, если необходимо
        }
    })
}

sealed class DataState {
    class Success(val data: MutableList<Messages>) : DataState()
    class Failure(val message: String) : DataState()
    object Loading: DataState()
    object Empty : DataState()
}
