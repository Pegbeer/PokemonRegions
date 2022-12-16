package com.example.pokemonregions.data.model

import java.io.Serializable

data class Pokemon(
    val id:Int? = 0,
    val name:String? = "",
    val types:List<PokemonType>? = emptyList()
):Serializable

data class PokemonType(
    val name: String? = ""
):Serializable
