package com.example.plantidentificationapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.plantidentificationapp.AuthenticationManager
import com.example.plantidentificationapp.DatabaseManager
import com.example.plantidentificationapp.GeoLocalizationManager
import com.example.plantidentificationapp.activities.HomeActivity
import com.example.plantidentificationapp.R
import com.example.plantidentificationapp.adapters.PlantAdapter
import com.example.plantidentificationapp.classes.MapMarkerData
import com.example.plantidentificationapp.classes.Plant
import com.example.plantidentificationapp.databinding.ActivityMyplantsBinding

var plantChosen : MapMarkerData? = null
class MyPlantsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMyplantsBinding
    private val databaseManager: DatabaseManager = DatabaseManager()
    private val authManager: AuthenticationManager = AuthenticationManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyplantsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Database
        databaseManager.getPlantsForUser(authManager.currentUser()!!)
        {
            myPlantList ->
            if (myPlantList.size != 0) {
                binding.listViewPlants.isClickable = true
                binding.listViewPlants.adapter = PlantAdapter(this, ArrayList(myPlantList))
                binding.listViewPlants.setOnItemClickListener { _, _, position, _ ->
                    val intent = Intent(this@MyPlantsActivity, PlantDetailsActivity::class.java)
                    plantChosen = myPlantList[position]
                    startActivity(intent)
                }
            } else {
                noResultsActivityStart()
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        binding = ActivityMyplantsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Database
        databaseManager.getPlantsForUser(authManager.currentUser()!!)
        {
                myPlantList ->
            if (myPlantList.size != 0) {
                binding.listViewPlants.isClickable = true
                binding.listViewPlants.adapter = PlantAdapter(this, ArrayList(myPlantList))
                binding.listViewPlants.setOnItemClickListener { _, _, position, _ ->
                    val intent = Intent(this@MyPlantsActivity, PlantDetailsActivity::class.java)
                    plantChosen = myPlantList[position]
                    startActivity(intent)
                }
            } else {
                noResultsActivityStart()
            }
        }
    }

    fun noResultsActivityStart () {
        setContentView(R.layout.activity_noresults)
        val noPlantsText = "No plants added yet"
        val noResultsTextView : TextView = findViewById(R.id.noresults_textview)
        noResultsTextView.text = noPlantsText
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@MyPlantsActivity, HomeActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}
