package com.example.plantidentificationapp.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.plantidentificationapp.AuthenticationManager
import com.example.plantidentificationapp.R
import com.example.plantidentificationapp.adapters.MenuAdapter
import com.example.plantidentificationapp.api.PlantAPI
import com.example.plantidentificationapp.api.createPlantAPIClient
import com.example.plantidentificationapp.api.model.ResponseIdentify
import com.example.plantidentificationapp.api.model.Suggestion
import com.example.plantidentificationapp.api.request.IdentifyRequest
import com.example.plantidentificationapp.classes.IdentifiedPlant
import com.example.plantidentificationapp.classes.MapType
import com.example.plantidentificationapp.classes.MenuItem
import com.example.plantidentificationapp.databinding.HomeScreenBinding
import com.example.plantidentificationapp.utils.encodeImageBase64

import kotlinx.android.synthetic.main.home_screen.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

const val PLANT_PROBABILITY_ACCEPTED = 0.65

// Photo related variables
lateinit var photoFile: File
private const val  FILE_NAME =  "photo.jpg"
private lateinit var takePictureIntent: Intent
private lateinit var fileProvider: Uri
private const val REQUEST_CODE = 200

private lateinit var progressDialog: ProgressDialog

// GLOBAL VARIABLE RESULT OF API
var identifiedPlantArrayList : ArrayList<IdentifiedPlant> = ArrayList()
var  responseIdentify : ArrayList<ResponseIdentify> = ArrayList()

class HomeActivity : AppCompatActivity() {
    private val logTag = "HomeActivity"

    private lateinit var authManager: AuthenticationManager
    // Main menu contents
    val menuImageId = intArrayOf(R.drawable.plants, R.drawable.watering, R.drawable.fertilizing)
    val menuItemName = listOf("My plants", "My plants map", "All plants map")
    private lateinit var menuItemArrayList : ArrayList<MenuItem>

    // Binding
    private lateinit var binding : HomeScreenBinding

    // API HANDLING
    val plantDetails : MutableList<String> = mutableListOf("common_names", "edible_parts", "propagation_methods", "scientific_name", "structured_name", "synonyms", "taxonomy", "url", "watering", "wiki_description", "wiki_image")
    val plantapi : PlantAPI by lazy { createPlantAPIClient() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.home_screen)
        binding = HomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMainMenuContents()

        authManager = AuthenticationManager(this)

        setupSignOutButton()

        progressDialog = ProgressDialog(this).apply {
            setMessage("Identifying...")
            setCancelable(false)
        }
    }

    private fun setupMainMenuContents() {
        menuItemArrayList = ArrayList()
        for(i in menuItemName.indices) {
            val singleMenuItem = MenuItem(menuItemName[i], menuImageId[i])
            menuItemArrayList.add(singleMenuItem)
        }
        binding.listViewMain.isClickable = true
        binding.listViewMain.adapter = MenuAdapter(this, menuItemArrayList)
        binding.listViewMain.setOnItemClickListener{_, _, position, _ ->
            when(position) {
                0 -> loadMyPlantsActivity()
                1 -> loadMapActivity(MapType.CURRENT_USER)
                2 -> loadMapActivity(MapType.ALL_USERS)
            }
        }

        // Camera button handling
        binding.buttonCamera.isClickable = true
        binding.buttonCamera.setOnClickListener{
            takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Getting the photo file and photo URI
            photoFile = getPhotoFile(FILE_NAME)
            fileProvider = FileProvider.getUriForFile(this, "com.example.fileprovider", photoFile)

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            }
            else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Getting temporary file in image directory
    private fun getPhotoFile(fileName: String) : File {
        val directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", directory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Activity of taking a picture
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this@HomeActivity, "Processing identification...", Toast.LENGTH_SHORT).show()
            // Sending image to API, assigning results to global ArrayList of identified plants and starting ChoosePlantActivity
            lifecycleScope.launch {
                progressDialog.show()
                val res = sendImageToAPI(encodeImageBase64(photoFile)).await()
                progressDialog.dismiss()
                responseIdentify.add(res)
                Log.d(logTag, "Plant probability: ${res.is_plant_probability}")
                if (res.is_plant && res.is_plant_probability >= PLANT_PROBABILITY_ACCEPTED) {
                    for (i in res.suggestions.indices) {
                        val singleSuggestion: Suggestion = res.suggestions.get(index = i)
                        val identifiedPlant = IdentifiedPlant(
                            singleSuggestion.id,
                            singleSuggestion.plant_details,
                            singleSuggestion.plant_name,
                            singleSuggestion.probability
                        )
                        identifiedPlantArrayList.add(identifiedPlant)
                    }
                }
                val intent = Intent(applicationContext, ChoosePlantActivity::class.java)
                intent.putExtra("image_path", photoFile.absolutePath)
                startActivity(intent)
            }
        }
        // Every other activity
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    // Sending image to api and returning a response
    private fun sendImageToAPI(encodedImageBase64: String)=
        lifecycleScope.async {
            val response = plantapi.identify(IdentifyRequest(images = listOf(encodedImageBase64), plant_details =  plantDetails))
            Log.d("MainActivity", "response: $response")
            return@async response
        }

    private fun setupSignOutButton() {
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

    private fun loadMapActivity(mapType: MapType) {
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("mapType", mapType)
        startActivity(intent)
    }

    private fun loadMyPlantsActivity () {
        val intent = Intent(this, MyPlantsActivity::class.java)
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
