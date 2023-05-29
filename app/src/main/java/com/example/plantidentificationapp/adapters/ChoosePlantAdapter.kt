package com.example.plantidentificationapp.adapters

import android.app.Activity
import android.database.sqlite.SQLiteException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.plantidentificationapp.R
import com.example.plantidentificationapp.classes.IdentifiedPlant
import com.squareup.picasso.Picasso

class ChoosePlantAdapter(private val context : Activity, private val identifiedPlantArrayList : ArrayList<IdentifiedPlant>) : ArrayAdapter<IdentifiedPlant>(context,
    R.layout.listitem_chooseplant, identifiedPlantArrayList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.listitem_chooseplant, null)
//        // Database
//        val dbHelper = DataBaseHelper(context)
//        val db = dbHelper.writableDatabase
        // Toasts
        val saveInfoToast = Toast.makeText(context, "Plant saved", Toast.LENGTH_SHORT)
        val saveInfoToast2 = Toast.makeText(context, "Saving plant...", Toast.LENGTH_SHORT)
        val errorToastInfo = Toast.makeText(context, "Null exception while saving", Toast.LENGTH_SHORT)
        // Contents
        val plantName : TextView = view.findViewById(R.id.chooseplant_name)
        val probability : TextView = view.findViewById(R.id.chooseplant_probability)
        val plantImage : ImageView = view.findViewById(R.id.chooseplant_image)
        val button : android.widget.Button = view.findViewById(R.id.chooseplant_button)
        val pictureLicense : TextView = view.findViewById(R.id.chooseplant_picture_license)

        plantName.text = identifiedPlantArrayList[position].plant_name
        probability.text = (identifiedPlantArrayList[position].probability*100).toInt().toString() + "%"
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
            val licenseConcatenated = identifiedPlantArrayList[position].plant_details.wiki_image?.license_name + ", " +  identifiedPlantArrayList[position].plant_details.wiki_image?.citation + ", " + identifiedPlantArrayList[position].plant_details.wiki_image?.license_url
            pictureLicense.text = licenseConcatenated
        } else {
            pictureLicense.visibility = View.GONE
        }
        // Adding plant to database
        button.setOnClickListener {
            try {
//                saveInfoToast2.show()
//                dbHelper.addPlantToDB(db, identifiedPlantArrayList[position])
//                saveInfoToast.show()
//                identifiedPlantArrayList.clear()
//                var intent = Intent(context, MyPlantsActivity::class.java)
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                context.startActivity(intent)
//                (context as Activity).finish()
            } catch (e : SQLiteException) {
                errorToastInfo.show()
                throw e
            }
        }
        return view
    }
}