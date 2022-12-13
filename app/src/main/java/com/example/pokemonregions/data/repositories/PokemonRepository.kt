package com.example.pokemonregions.data.repositories

import com.example.pokemonregions.data.model.PokemonTeam
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val database:FirebaseDatabase
) {

    private val pokemonTeamDatabase:DatabaseReference by lazy { database.getReference(PokemonTeam::class.java.simpleName) }

    private val user by lazy { FirebaseAuth.getInstance().currentUser }

    suspend fun saveTeam(team: PokemonTeam):PokemonTeam{
        val uuid = UUID.randomUUID()
        team.id = uuid.toString()
        team.userId = user!!.uid
        pokemonTeamDatabase.child(team.id).setValue(team).await()
        return team
    }

    suspend fun lastTeamNumber():Int{
        val user = FirebaseAuth.getInstance().currentUser!!
        val queryTeamCount = pokemonTeamDatabase
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