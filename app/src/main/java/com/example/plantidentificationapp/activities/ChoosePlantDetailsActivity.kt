package com.example.plantidentificationapp.activities

import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.plantidentificationapp.R
import com.example.plantidentificationapp.api.model.PlantDetails
import com.example.plantidentificationapp.databinding.ActivityChooseplantdetailsBinding
//import com.example.plantidentificationapp.database.DataBaseHelper
//import com.example.plantidentificationapp.databinding.ActivityChooseplantdetailsBinding
import com.example.plantidentificationapp.utils.nullableListStringIntoString
import com.squareup.picasso.Picasso

class ChoosePlantDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseplantdetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseplantdetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val dbHelper = DataBaseHelper(applicationContext)
//        val db = dbHelper.writableDatabase
        val saveInfoToast = Toast.makeText(applicationContext, "Plant saved", Toast.LENGTH_SHORT)
        val saveInfoToast2 = Toast.makeText(applicationContext, "Saving plant...", Toast.LENGTH_SHORT)
        val errorToastInfo = Toast.makeText(applicationContext, "Null exception while saving", Toast.LENGTH_SHORT)

        val picture : ImageView = binding.chooseplantdetailsPicture
        val commonName : TextView = binding.chooseplantdetailsCommonName
        val scientificName : TextView = binding.chooseplantdetailsScientificname
        val probability : TextView = binding.chooseplantdetailsProbability
        val descriptionCardView : CardView = binding.chooseplantdetailsDescriptionCardview
        val description : TextView = binding.chooseplantdetailsDescription
        val citation : TextView = binding.chooseplantdetailsCitation
        val licenseName : TextView = binding.chooseplantdetailsLicenseName
        val synonymsCardView : CardView = binding.chooseplantdetailsSynonymsCardview
        val synonyms : TextView = binding.chooseplantdetailsSynonyms
        val propagationMethodsCardView : CardView = binding.chooseplantdetailsPropagationmethodsCardview
        val propagationMethods : TextView = binding.chooseplantdetailsPropagationmethods
        val ediblePartsCardView : CardView = binding.chooseplantdetailsEdiblepartsCardview
        val edibleParts : TextView = binding.chooseplantdetailsEdibleparts
        val structuredNameCardView : CardView = binding.chooseplantdetailsStructurednameCardview
        val genus : TextView = binding.chooseplantdetailsStructurednameGenus
        val species : TextView = binding.chooseplantdetailsStructurednameSpecies
        val taxonomyCardView : CardView = binding.chooseplantdetailsTaxonomyCardview
        val taxonomyClass : TextView = binding.chooseplantdetailsTaxonomyClass
        val taxonomyFamily : TextView = binding.chooseplantdetailsTaxonomyFamily
        val taxonomyGenus : TextView = binding.chooseplantdetailsTaxonomyGenus
        val taxonomyKingdom : TextView = binding.chooseplantdetailsTaxonomyKingdom
        val taxonomyPhylum : TextView = binding.chooseplantdetailsTaxonomyPhylum
        val button : Button = binding.chooseplantdetailsAddplantButton
        val pictureLicense : TextView = binding.chooseplantdetailsPictureLicense

        if (chosenPlant != null) {
            val plantDetails : PlantDetails? = chosenPlant?.plant_details

            // Picture
            if (plantDetails?.wiki_image?.value != null) {
                Picasso.with(applicationContext)
                    .load(plantDetails.wiki_image.value)
                    .into(picture)
            } else {
                picture.setImageResource(R.drawable.noimage)
            }
            // Picture license
            if (plantDetails?.wiki_image?.citation != null || plantDetails?.wiki_image?.license_name != null || plantDetails?.wiki_image?.license_url != null) {
                pictureLicense.visibility = View.VISIBLE
                val licenseConcatenated = plantDetails?.wiki_image?.license_name + ", " +  plantDetails?.wiki_image?.citation + ", " + plantDetails?.wiki_image?.license_url
                pictureLicense.text = licenseConcatenated
            } else {
                pictureLicense.visibility = View.GONE
            }
            // Plant details
            commonName.text = chosenPlant?.plant_name
            scientificName.text = plantDetails?.scientific_name
            if (chosenPlant?.probability != null) {
                probability.text = (chosenPlant!!.probability * 100).toInt().toString() + "%"
            }
            // Description
            if (plantDetails?.wiki_description != null) {
                descriptionCardView.visibility = View.VISIBLE
                description.text = plantDetails.wiki_description.value
                citation.text = plantDetails.wiki_description.citation
                licenseName.text = plantDetails.wiki_description.license_name
            } else {
                descriptionCardView.visibility = View.GONE
            }
            // Synonyms
            if (plantDetails?.synonyms != null) {
                synonymsCardView.visibility = View.VISIBLE
                synonyms.text = nullableListStringIntoString(plantDetails.synonyms)
            } else {
                synonymsCardView.visibility = View.GONE
            }
            // Propagation methods
            if (plantDetails?.propagation_methods != null) {
                propagationMethodsCardView.visibility = View.VISIBLE
                propagationMethods.text =
                    nullableListStringIntoString(plantDetails.propagation_methods)
            } else {
                propagationMethodsCardView.visibility = View.GONE
            }
            // Edible parts
            if (plantDetails?.edible_parts != null) {
                ediblePartsCardView.visibility = View.VISIBLE
                edibleParts.text = nullableListStringIntoString(plantDetails.edible_parts)
            } else {
                ediblePartsCardView.visibility = View.GONE
            }
            // Structured name
            if (plantDetails?.structured_name != null) {
                structuredNameCardView.visibility = View.VISIBLE
                genus.text = plantDetails.structured_name.genus
                species.text = plantDetails.structured_name.species
            } else {
                structuredNameCardView.visibility = View.GONE
            }
            // Taxonomy
            if (plantDetails?.taxonomy != null) {
                taxonomyCardView.visibility = View.VISIBLE
                taxonomyClass.text = plantDetails.taxonomy.`class`
                taxonomyFamily.text = plantDetails.taxonomy.family
                taxonomyGenus.text = plantDetails.taxonomy.genus
                taxonomyKingdom.text = plantDetails.taxonomy.kingdom
                taxonomyPhylum.text = plantDetails.taxonomy.phylum
            } else {
                taxonomyCardView.visibility = View.GONE
            }
            // Adding plant to database
            button.setOnClickListener {
                try {
//                    saveInfoToast2.show()
//                    dbHelper.addPlantToDB(db, chosenPlant!!)
//                    saveInfoToast.show()
//                    chosenPlant = null
//                    identifiedPlantArrayList.clear()
//                    var intent = Intent(applicationContext, MyPlantsActivity::class.java)
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                    startActivity(intent)
//                    finish()
                } catch (e : SQLiteException) {
                    errorToastInfo.show()
                    throw e
                }
            }
        }
    }
}