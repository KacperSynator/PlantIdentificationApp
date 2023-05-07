package com.example.plantidentificationapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.plantidentificationapp.R
import com.example.plantidentificationapp.adapters.ChoosePlantAdapter
import com.example.plantidentificationapp.classes.IdentifiedPlant
import com.example.plantidentificationapp.databinding.ActivityChooseplantBinding
import com.example.plantidentificationapp.identifiedPlantArrayList
import com.example.plantidentificationapp.photoFile
import com.example.plantidentificationapp.responseIdentify

// UWAGA ZMIENNA GLOBALNA
var chosenPlant: IdentifiedPlant? = null

class ChoosePlantActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseplantBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseplantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chosenPlant = null

        if (identifiedPlantArrayList.size != 0) {
            binding.listViewChooseplant.isClickable = true
            binding.listViewChooseplant.adapter = ChoosePlantAdapter(this, identifiedPlantArrayList)
            binding.listViewChooseplant.setOnItemClickListener { _, _, position, _ ->
                chosenPlant = identifiedPlantArrayList.get(index = position)

                val imageFile = intent.getStringExtra("image_path")
                val intent = Intent(this@ChoosePlantActivity, ChoosePlantDetailsActivity::class.java)
                intent.putExtra("image_path", imageFile)
                startActivity(intent)
            }
        } else {
            noResultsActivityStart()
        }
    }

    fun noResultsActivityStart() {
        setContentView(R.layout.activity_noresults)
        val noPlantsText = "No plants identified"
        val noResultsTextView: TextView = findViewById(R.id.noresults_textview)
        noResultsTextView.text = noPlantsText
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
        responseIdentify.clear()
        identifiedPlantArrayList.clear()
    }
}