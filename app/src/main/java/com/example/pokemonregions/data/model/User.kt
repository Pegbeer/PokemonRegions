package com.example.pokemonregions.data.model

data class User(
    val id:String,
    val name:String,
    val email:String,
    val teams:List<PokemonTeam>
)
