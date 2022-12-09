package com.example.pokemonregions.data.model

data class Region(val name:String)

data class RegionResponse(
    val count:Int,
    val results:List<Region>
)
