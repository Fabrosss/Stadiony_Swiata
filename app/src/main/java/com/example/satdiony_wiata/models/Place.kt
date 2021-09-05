package com.example.satdiony_wiata.models

import java.io.Serializable

class Place (
    var title: String,
    var description: String,
    val latitude: Double,
    val longitude: Double
    ) : Serializable
