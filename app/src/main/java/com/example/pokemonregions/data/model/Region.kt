package com.example.pokemonregions.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Region(
    val id:Int,
    val name:String,
    val generation: Generation
): java.io.Serializable

data class RegionResponse(
    val count:Int,
    val results:List<Region>
)
