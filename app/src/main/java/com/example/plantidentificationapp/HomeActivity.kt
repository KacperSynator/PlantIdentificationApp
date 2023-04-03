package com.example.plantidentificationapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.plantidentificationapp.adapters.MenuAdapter
import com.example.plantidentificationapp.classes.MenuItem
import com.example.plantidentificationapp.databinding.HomeScreenBinding

import kotlinx.android.synthetic.main.home_screen.*

class HomeActivity : AppCompatActivity() {
    private lateinit var authManager: AuthenticationManager
    // Main menu contents
    val menuImageId = intArrayOf(R.drawable.plants, R.drawable.watering, R.drawable.fertilizing)
    val menuItemName = listOf("My plants", "Watering", "Fertilizing")
    private lateinit var menuItemArrayList : ArrayList<MenuItem>

    // Binding
    private lateinit var binding : HomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setting up binding
        setContentView(R.layout.home_screen)
        binding = HomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setting up main menu contents
        menuItemArrayList = ArrayList()
        for(i in menuItemName.indices) {
            val singleMenuItem = MenuItem(menuItemName[i], menuImageId[i])
            menuItemArrayList.add(singleMenuItem)
        }
        binding.listViewMain.isClickable = true
        binding.listViewMain.adapter = MenuAdapter(this, menuItemArrayList)

        authManager = AuthenticationManager(this)

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
