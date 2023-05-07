package com.example.plantidentificationapp.api.request

data class IdentifyRequest(
    val images : List<String>,
    val plant_details : List<String>,
    val api_key : String = "5RrDFo2WwgYxvHDJTwL8VMYrhsbeMNqaVWfAu7q7WSc1Gd2PMW"
)
