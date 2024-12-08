package com.example.plantidentificationapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.plantidentificationapp.AuthenticationManager
import com.example.plantidentificationapp.R
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.login_screen.*

class LoginActivity : AppCompatActivity() {
    private val logTag = "LoginActivity"

    private lateinit var authManager: AuthenticationManager

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = authManager.googleSignInTask(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!

                val responseCallback: (Boolean) -> Unit = { success ->
                    if (success) {
                        loadHomeActivity()
                    }

                    toastResult(
                        success,
                        "Welcome, ${authManager.currentUser()?.displayName}",
                        "Authentication failed."
                    )
                }

                authManager.logInWithGoogle(account, responseCallback)

            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In failed.", Toast.LENGTH_SHORT).show()
                Log.e(logTag, "Google Sign-In failed. ${e}")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authManager = AuthenticationManager(this)

        if (authManager.isCurrentUserSignedIn()) {
            loadHomeActivity()
        } else {
            loadLoginScreen()
        }
    }

    data class GetEmailAndPasswordResult(
        val success: Boolean, val email: String, val password: String
    )

    private fun getEmailAndPassword(): GetEmailAndPasswordResult {
        val email = login_edit_email.text.toString()
        val password = login_edit_password.text.toString()
        val repeat_password = login_edit_repeat_password.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return GetEmailAndPasswordResult(false, email, password)
        }

        if (password != repeat_password) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return GetEmailAndPasswordResult(false, email, password)
        }

        return GetEmailAndPasswordResult(true, email, password)
    }

    private fun toastResult(success: Boolean, onSuccess: String, onFailure: String) {
        if (success) {
            Toast.makeText(this, onSuccess, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, onFailure, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadLoginScreen() {
        setContentView(R.layout.login_screen)

        logging_button.setOnClickListener {
            val result = getEmailAndPassword()

            if (!result.success) {
                return@setOnClickListener
            }

            val responseCallback: (Boolean) -> Unit = { success ->
                toastResult(
                    success,
                    "Welcome, ${authManager.currentUser()?.displayName}",
                    "Authentication failed."
                )

                if (success) {
                    loadHomeActivity()
                }
            }

            authManager.logInWithEmailAndPassword(result.email, result.password, responseCallback)
        }

        sign_up_button.setOnClickListener {
            val result = getEmailAndPassword()

            if (!result.success) {
                return@setOnClickListener
            }

            val responseCallback: (Boolean) -> Unit = { success ->
                toastResult(
                    success, "Account created successfully", "Failed to create account."
                )
            }

            authManager.createAccountWithEmailAndPassword(
                result.email, result.password, responseCallback
            )
        }

        switch_between_login_and_sign_up.setOnClickListener {
            if (login_edit_repeat_password.visibility == android.view.View.VISIBLE) {
                switch_between_login_and_sign_up.text = "Sign Up"
                sign_up_prompt.text = "Already have an account?"
                login_text_repeat_password.visibility = android.view.View.GONE
                login_edit_repeat_password.visibility = android.view.View.GONE
                sign_up_button.visibility = android.view.View.GONE
                logging_button.visibility = android.view.View.VISIBLE
            } else {
                switch_between_login_and_sign_up.text = "Log In"
                sign_up_prompt.text = "Don't have an account?"
                login_edit_repeat_password.visibility = android.view.View.VISIBLE
                login_text_repeat_password.visibility = android.view.View.VISIBLE
                sign_up_button.visibility = android.view.View.VISIBLE
                logging_button.visibility = android.view.View.GONE
            }
        }

        google_sign_in_button.setOnClickListener {
            val signInIntent = authManager.createSignInIntent()
            signInLauncher.launch(signInIntent)
        }
    }

    private fun loadHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}
