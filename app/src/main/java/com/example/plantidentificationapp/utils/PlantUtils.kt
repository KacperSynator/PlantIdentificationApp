package com.example.plantidentificationapp.utils

import com.example.plantidentificationapp.classes.IdentifiedPlant

fun returnPlantDaysWatering (identifiedPlant: IdentifiedPlant) : Int {
    val watering = identifiedPlant.plant_details.watering
    if (watering != null) {
        val min = identifiedPlant.plant_details.watering?.min
        val max = identifiedPlant.plant_details.watering?.max
        if (max == 1) {
            return 14
        } else if (min == 3) {
            return 3
        } else if (min == 2 && max == 3) {
            return 10
        } else if (min == 2 && max == 2) {
            return 7
        } else if (min == 1 && max == 2) {
            return 5
        } else if (min == 1 && max == 3) {
            return 9
        }
    }
    return 7 // Default value
}

fun nullableListStringIntoString (list : List<String>?) : String {
    var text = ""
    if (list != null) {
        list?.let {
            for (i in it.indices) {
                if (i == 0) {
                    text += it.get(index = i)
                } else {
                    text += "\n" + it.get(index = i)
                }
            }
        }
    }
    return text
}