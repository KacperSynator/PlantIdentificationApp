package com.example.plantidentificationapp

import android.location.Location
import android.net.Uri
import android.util.Log
import com.example.plantidentificationapp.classes.MapMarkerData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.net.URL


class DatabaseManager {
    private val logTag = "DatabaseManager"

    private val database = FirebaseDatabase.getInstance().reference
    private val storage = FirebaseStorage.getInstance()

    companion object {
        const val IDENTIFIED_PLANTS = "identified_plants"
        const val USERS = "users"
        const val PLANT_IMAGES = "plant_images"
    }

    init {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    fun addPlant(
        user: FirebaseUser,
        location: Location,
        title: String,
        description: String,
        image: File
    ) {
        val userIdentifiedPlants = database.child(USERS).child(user.uid).child(IDENTIFIED_PLANTS)
        val plantId = userIdentifiedPlants.push().key

        uploadPlantImage(image, plantId!!) { url ->
            if (url != null) {
                Log.i(logTag, "Image uploaded url: $url")
                val plantData = MapMarkerData(location, title, description, url)
                userIdentifiedPlants.child(plantId).setValue(plantData)
            } else {
                Log.e(logTag, "Failed to upload image plantId: $plantId")
            }
        }
    }

    fun getPlantsForUser(user: FirebaseUser, callback: (List<MapMarkerData>) -> Unit) {
        val plantsRef = database.child(USERS).child(user.uid).child(IDENTIFIED_PLANTS)

        val plantsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val plants = mutableListOf<MapMarkerData>()
                for (plantSnapshot in snapshot.children) {
                    val plant = plantSnapshot.getValue(MapMarkerData::class.java)
                    if (plant != null) {
                        plants.add(plant)
                    }
                }
                callback(plants)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(logTag, "Error retrieving plants for user ${user.uid}: ${error.message}")
                callback(emptyList())
            }
        }

        plantsRef.addValueEventListener(plantsListener)
    }

    fun getAllPlants(callback: (List<MapMarkerData>) -> Unit) {
        val usersRef = database.child(USERS)
        val plants = mutableListOf<MapMarkerData>()

        val usersListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children) {
                    for (plantSnapshot in userSnapshot.child(IDENTIFIED_PLANTS).children) {
                        val plant = plantSnapshot.getValue(MapMarkerData::class.java)
                        if (plant != null) {
                            plants.add(plant)
                        }
                    }
                }

                callback(plants)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(logTag, "Error retrieving users: ${error.message}")
                callback(emptyList())
            }
        }

        usersRef.addValueEventListener(usersListener)
    }

    private fun uploadPlantImage(imageFile: File, imageId: String, callback: (URL?) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().getReference(PLANT_IMAGES)
        val imageRef = storageRef.child("$imageId.jpg")

        val uploadTask = imageRef.putFile(Uri.fromFile(imageFile))

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    Log.e(logTag, "Exception when uploading image: ${it.message}")
                    callback(null)
                }
            }

            imageRef.downloadUrl

        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val imageUrl = task.result.toString()

                callback(URL(imageUrl))
            } else {
                Log.e(logTag, "Task failed to upload image")
                callback(null)
            }
        }
    }
}
