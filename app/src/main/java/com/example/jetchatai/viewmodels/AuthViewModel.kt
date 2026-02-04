package com.example.jetchatai.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class AuthViewModel : ViewModel(){
    val auth = Firebase.auth
    val db = Firebase.firestore

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    val currentUser = auth.currentUser

    fun signUp(name: String, email: String, pass: String, onSuccess: () -> Unit) {
        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            errorMessage.value = "Please fill all fields"
            return
        }
        errorMessage.value = null
        isLoading.value = true

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val userMap = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "createdAt" to System.currentTimeMillis()
                    )

                    if (userId != null) {
                        db.collection("users").document(userId).set(userMap)
                            .addOnCompleteListener { firestoreTask ->
                                isLoading.value = false // Stop loading here
                                if (firestoreTask.isSuccessful) {
                                    onSuccess()
                                } else {
                                    errorMessage.value = firestoreTask.exception?.message
                                }
                            }
                    }
                } else {
                    isLoading.value = false
                    errorMessage.value = task.exception?.message
                }
            }
    }

    fun signUpWithCode(name: String, email: String, pass: String, onCodeSent: () -> Unit) {
        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            errorMessage.value = "Please fill all fields"
            return
        }

        isLoading.value = true
        errorMessage.value = null

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                    val verificationCode = (100000..999999).random().toString()

                    val userMap = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "verificationCode" to verificationCode,
                        "isVerified" to false,
                        "createdAt" to System.currentTimeMillis()
                    )

                    db.collection("users").document(userId).set(userMap)
                        .addOnCompleteListener { firestoreTask ->
                            isLoading.value = false
                            if (firestoreTask.isSuccessful) {

                                println("DEBUG: Code for $email is $verificationCode")
                                onCodeSent()
                            } else {
                                errorMessage.value = firestoreTask.exception?.message
                            }
                        }
                } else {
                    isLoading.value = false
                    errorMessage.value = task.exception?.message
                }
            }
    }

    fun verifyCode(userInput: String, onSuccess: () -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        isLoading.value = true
        errorMessage.value = null

        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                val correctCode = document.getString("verificationCode")

                if (userInput == correctCode) {
                    db.collection("users").document(userId).update("isVerified", true)
                        .addOnSuccessListener {
                            isLoading.value = false
                            onSuccess()
                        }
                        .addOnFailureListener {
                            isLoading.value = false
                            errorMessage.value = it.message
                        }
                } else {
                    isLoading.value = false
                    errorMessage.value = "Incorrect code. Please try again."
                }
            }
            .addOnFailureListener {
                isLoading.value = false
                errorMessage.value = it.message
            }
    }

    fun login(email: String, pass: String, onSuccess: () -> Unit) {
        if (email.isEmpty() || pass.isEmpty()) {
            errorMessage.value = "Please fill all fields"
            return
        }
        isLoading.value = true
        errorMessage.value = null

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    errorMessage.value = task.exception?.message
                }
            }
    }

}