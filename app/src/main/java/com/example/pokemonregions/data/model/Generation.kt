package com.example.pokemonregions.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Generation(
    val id:Int,
    val name:String
) : java.io.Serializable
