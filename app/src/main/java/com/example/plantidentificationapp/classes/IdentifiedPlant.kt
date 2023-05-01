package com.example.plantidentificationapp.classes

import com.example.plantidentificationapp.api.model.PlantDetails

class IdentifiedPlant(var id: Int,
                      var plant_details: PlantDetails,
                      var plant_name: String,
                      var probability: Double)
