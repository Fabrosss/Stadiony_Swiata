package com.example.satdiony_wiata.models

import java.io.Serializable

data class Stadiums (
    var title: String,
    val places: List<Place>
        ) :Serializable
