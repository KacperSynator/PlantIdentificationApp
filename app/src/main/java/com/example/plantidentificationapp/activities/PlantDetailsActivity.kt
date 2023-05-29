package com.example.plantidentificationapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.example.plantidentificationapp.R
import com.example.plantidentificationapp.databinding.ActivityPlantdetailsBinding

class PlantDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPlantdetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantdetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toasts
        val errorToast : Toast = Toast.makeText(applicationContext, "Error! No plant chosen.", Toast.LENGTH_SHORT)
        // Contents
        val picture : ImageView = binding.plantdetailsPicture
        val commonName : TextView = binding.commonName
        val scientificName : TextView = binding.latinName
        val descriptionCardView : CardView = binding.plantdetailsDescriptionCardview
        val description : TextView = binding.plantdetailsDescription

        if (plantChosen != null) {
            // Picture
            if (plantChosen?.imageURL != null) {
                Glide.with(this)
                    .load(plantChosen?.imageURL)
                    .into(picture)
            }
            else {
                picture.setImageResource(R.drawable.noimage)
            }
            // Plant details
            commonName.text = plantChosen?.title
            scientificName.text = plantChosen?.title
            // Description
            if (plantChosen?.description != null && plantChosen?.description?.length != 0) {
                descriptionCardView.visibility = View.VISIBLE
                description.text = plantChosen?.description
            } else {
                descriptionCardView.visibility = View.GONE
            }
        } else {
            onBackPressed()
            errorToast.show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
        plantChosen = null
    }
}