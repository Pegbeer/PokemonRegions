package com.example.pokemonregions.data.model

data class Pokemon(
    val id:Int,
    val name:String,
    val types:List<PokemonType>
)

data class PokemonType(
    val name: String?
)
