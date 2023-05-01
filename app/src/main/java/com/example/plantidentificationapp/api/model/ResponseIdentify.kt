package com.example.plantidentificationapp.api.model

data class ResponseIdentify(
    val countable: Boolean,
    val custom_id: Any,
    val fail_cause: Any,
    val feedback: Any,
    val finished_datetime: Double,
    val id: Int,
    val images: List<Image>,
    val is_plant: Boolean,
    val is_plant_probability: Double,
    val meta_data: MetaData,
    val modifiers: List<String>,
    val secret: String,
    val suggestions: List<Suggestion>,
    val uploaded_datetime: Double
)