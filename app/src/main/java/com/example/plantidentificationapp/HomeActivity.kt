package com.example.plantidentificationapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.home_screen.*

class HomeActivity : AppCompatActivity() {
    private lateinit var authManager: AuthenticationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authManager = AuthenticationManager(this)

        setContentView(R.layout.home_screen)

        home_screen_sign_out_button.setOnClickListener {
            val responseCallback: (Boolean) -> Unit = { success ->
                toastResult(
                    success, "Signed out successfully.", "Signed out failed."
                )

                if (success) {
                    loadLoginActivity()
                }
            }

            authManager.signOut(responseCallback)
        }
    }

    private fun loadLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun toastResult(success: Boolean, onSuccess: String, onFailure: String) {
        if (success) {
            Toast.makeText(this, onSuccess, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, onFailure, Toast.LENGTH_SHORT).show()
        }
    }
}
