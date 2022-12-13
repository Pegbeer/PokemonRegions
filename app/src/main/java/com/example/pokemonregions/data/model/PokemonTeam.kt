package com.example.pokemonregions.data.model

data class PokemonTeam(
    var id:String,
    var userId:String,
    val name:String,
    val number:Int,
    val pokemons:List<Pokemon>
)