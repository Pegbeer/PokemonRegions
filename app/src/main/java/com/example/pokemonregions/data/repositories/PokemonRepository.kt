package com.example.pokemonregions.data.repositories

import com.example.pokemonregions.data.model.Pokemon
import com.example.pokemonregions.data.model.PokemonTeam
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val database:FirebaseDatabase
) {

    private val teamedDatabase:DatabaseReference by lazy { database.getReference(PokemonTeam::class.java.simpleName) }

    private val user by lazy { FirebaseAuth.getInstance().currentUser }

    suspend fun saveTeam(name:String,pokemons:List<Pokemon>):PokemonTeam{

        val uuid = UUID.randomUUID()
        val id = uuid.toString()
        val lastTeam = lastTeamNumber()
        val userId = user!!.uid
        val pokemonTeam = PokemonTeam().apply {
            setId(id)
            setName(name)
            setUserId(userId)
            setPokemons(pokemons)
            number = lastTeam + 1
        }
        teamedDatabase.child(pokemonTeam.id).setValue(pokemonTeam).await()
        return pokemonTeam
    }

    private suspend fun lastTeamNumber():Int{
        val user = FirebaseAuth.getInstance().currentUser!!
        val queryTeamCount = teamedDatabase
            .orderByChild("userId")
            .equalTo(user.uid)
            .get()
            .await()
        return if(queryTeamCount.exists() && queryTeamCount.hasChildren()){
            queryTeamCount.childrenCount.toInt()
        }else{
            0
        }
    }
}