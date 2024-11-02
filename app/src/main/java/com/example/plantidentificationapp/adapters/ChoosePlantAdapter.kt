package com.example.plantidentificationapp.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.plantidentificationapp.*
import com.example.plantidentificationapp.activities.MyPlantsActivity
import com.example.plantidentificationapp.classes.IdentifiedPlant
import com.squareup.picasso.Picasso
import java.io.File

class ChoosePlantAdapter(
    private val context: Activity,
    private val identifiedPlantArrayList: ArrayList<IdentifiedPlant>
) : ArrayAdapter<IdentifiedPlant>(
    context,
    R.layout.listitem_chooseplant, identifiedPlantArrayList
) {

    private val logTag = "ChoosePlantDetailsActivity"

    // Database
    private val databaseManager: DatabaseManager = DatabaseManager()
    private val geoManager: GeoLocalizationManager = GeoLocalizationManager(context)
    private val authManager: AuthenticationManager = AuthenticationManager(context)

    // Toasts
    val saveInfoToast = Toast.makeText(context, "Plant saved", Toast.LENGTH_SHORT)
    val saveInfoToast2 = Toast.makeText(context, "Saving plant...", Toast.LENGTH_SHORT)
    val errorToastInfo = Toast.makeText(context, "Null exception while saving", Toast.LENGTH_SHORT)
    val savedInfoToast = Toast.makeText(context, "Plant saved", Toast.LENGTH_SHORT)
    val dbToastError =
        Toast.makeText(
            context,
            "Failed to save plant in database",
            Toast.LENGTH_SHORT
        )
    val alreadySavedErrorToast =
        Toast.makeText(context, "Plant is already saved", Toast.LENGTH_SHORT)
    var isPlantSaved = false

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.listitem_chooseplant, null)

        // Database
        val databaseManager: DatabaseManager = DatabaseManager()
        val geoManager: GeoLocalizationManager = GeoLocalizationManager(context)
        val authManager: AuthenticationManager = AuthenticationManager(context)

        // Contents
        val plantName: TextView = view.findViewById(R.id.chooseplant_name)
        val probability: TextView = view.findViewById(R.id.chooseplant_probability)
        val plantImage: ImageView = view.findViewById(R.id.chooseplant_image)
        val button: android.widget.Button = view.findViewById(R.id.chooseplant_button)
        val pictureLicense: TextView = view.findViewById(R.id.chooseplant_picture_license)

        plantName.text = identifiedPlantArrayList[position].plant_name
        probability.text =
            (identifiedPlantArrayList[position].probability * 100).toInt().toString() + "%"
        // Setting the image
        if (identifiedPlantArrayList[position].plant_details.wiki_image?.value != null) {
            Picasso.with(view.context)
                .load(identifiedPlantArrayList[position].plant_details.wiki_image?.value)
                .into(plantImage)
        } else {
            plantImage.setImageResource(R.drawable.noimage)
        }
        // Image license
        if (identifiedPlantArrayList[position].plant_details.wiki_image?.license_name != null || identifiedPlantArrayList[position].plant_details.wiki_image?.citation != null || identifiedPlantArrayList[position].plant_details.wiki_image?.license_url != null) {
            pictureLicense.visibility = View.VISIBLE
            val licenseConcatenated =
                identifiedPlantArrayList[position].plant_details.wiki_image?.license_name + ", " + identifiedPlantArrayList[position].plant_details.wiki_image?.citation + ", " + identifiedPlantArrayList[position].plant_details.wiki_image?.license_url
            pictureLicense.text = licenseConcatenated
        } else {
            pictureLicense.visibility = View.GONE
        }
        // Adding plant to database
        button.setOnClickListener {
            try {
                if (isPlantSaved) {
                    Log.e(logTag, "Plant is already saved")
                    alreadySavedErrorToast.show()
                    return@setOnClickListener
                }

                geoManager.getLocation { location ->
                    if (location == null) {
                        Log.e(logTag, "Location is null user: ${authManager.currentUser()!!.uid}")
                        return@getLocation
                    }
                    saveInfoToast2.show()
                    databaseManager.addPlant(
                        authManager.currentUser()!!,
                        location,
                        identifiedPlantArrayList[position].plant_name.toString(),
                        identifiedPlantArrayList[position].plant_details?.wiki_description?.value.toString(),
                        File(context.intent.getStringExtra("image_path")!!)
                    ) { isSuccess ->
                        if (isSuccess) {
                            savedInfoToast.show()
                            isPlantSaved = true
                            identifiedPlantArrayList.clear()
                            var intent = Intent(context, MyPlantsActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            context.startActivity(intent)
                            (context).finish()
                        } else {
                            dbToastError.show()
                        }
                    }
                }
            } catch (e: Exception) {
                errorToastInfo.show()
                throw e
            }
        }
        return view
    }
}