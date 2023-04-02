package com.example.plantidentificationapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class AuthenticationManager(private val context: Context) {
    val logTag = "AuthenticationManager"

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val googleSignInClient by lazy {
        GoogleSignIn.getClient(
            context, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
    }

    fun createSignInIntent(): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)

        return googleSignInClient.signInIntent
    }

    fun isCurrentUserSignedIn(): Boolean {
        return auth.currentUser != null
    }

    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun googleSignInTask(intent: Intent?): Task<GoogleSignInAccount> {
        return GoogleSignIn.getSignedInAccountFromIntent(intent)
    }

    fun createAccountWithEmailAndPassword(
        email: String,
        password: String,
        callback: (success: Boolean) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e(logTag, "Email and password user creation failed: ${task.exception}")
                    Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                }

                callback(task.isSuccessful)
            }
    }

    fun logInWithEmailAndPassword(
        email: String,
        password: String,
        callback: (success: Boolean) -> Unit
    ) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(logTag, "Email and password login failed: ${task.exception}")
            }

            callback(task.isSuccessful)
        }
    }

    fun logInWithGoogle(
        acct: GoogleSignInAccount,
        callback: (success: Boolean) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(logTag, "Google authentication failed: ${task.exception}")
            }

            callback(task.isSuccessful)
        }
    }

    fun signOut(callback: (success: Boolean) -> Unit) {
        auth.signOut()

        googleSignInClient.signOut().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(logTag, "Google sign out: ${task.exception}")
            }

            callback(task.isSuccessful)
        }
    }
}
