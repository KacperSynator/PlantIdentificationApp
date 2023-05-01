package com.example.plantidentificationapp.classes

import android.graphics.Bitmap
import java.time.LocalDate

class Plant (var id : Int,
             var idApi : Int,
             var name : String,
             var picture : Bitmap,
             var dateAdded : LocalDate,
             var dateWatered : LocalDate,
             var daysWatering : Int,
             var dateFertilized : LocalDate,
             var daysFertilizing : Int,
             var scientificName : String?,
             var commonNames : String?,
             var edibleParts : String?,
             var propagationMethods : String?,
             var structuredNameGenus : String?,
             var structuredNameSpecies : String?,
             var synonyms : String?,
             var taxonomyClass : String?,
             var taxonomyFamily : String?,
//             var taxonomyGenus : String?, // TODO : DELETED
             var taxonomyKingdom : String?,
             var taxonomyOrder : String?,
             var taxonomyPhylum : String?,
             val descriptionValue : String?,
             val descriptionLicense : String?,
             val descriptionCitation : String?
)

