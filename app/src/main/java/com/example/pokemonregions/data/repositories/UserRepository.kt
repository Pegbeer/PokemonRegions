package com.example.pokemonregions.data.repositories

import android.util.Log
import com.example.pokemonregions.core.Result
import com.example.pokemonregions.data.model.PokemonTeam
import com.example.pokemonregions.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val database:FirebaseDatabase
) {

    private val gson by lazy { Gson() }

    private val teamTypeToken = object : TypeToken<ArrayList<PokemonTeam>>(){}.type

    private val databaseUser:DatabaseReference by lazy { database.getReference(User::class.java.simpleName)}
    private val pokemonDatabase:DatabaseReference by lazy { database.getReference(PokemonTeam::class.java.simpleName) }

    fun createUser(user:User):User{
        databaseUser.child(user.id).setValue(user)
        return user
    }

    fun checkUserExists(email:String?) = callbackFlow<Result<Boolean>>{
        val checkUserQuery = databaseUser.orderByChild("email").equalTo(email)
        val checkListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                this@callbackFlow.trySendBlocking(Result.success(snapshot.exists()))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.error(error.message,null))
            }
        }

        checkUserQuery.addValueEventListener(checkListener)

        awaitClose{
            checkUserQuery.removeEventListener(checkListener)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun fetchTeams() = callbackFlow<Result<List<PokemonTeam?>>>{
        val user = FirebaseAuth.getInstance().currentUser!!
        val query = pokemonDatabase.orderByChild("userId").equalTo(user.uid)
        val queryListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists() && snapshot.hasChildren()){
                    val data = snapshot.children.map {
                        val hash = it.value as HashMap<*, *>
                        val json = gson.toJson(hash)
                        val obj = gson.fromJson(json,PokemonTeam::class.java)
                        obj
                    }
                    trySendBlocking(Result.success(data))
                }else{
                    trySendBlocking(Result.success(emptyList()))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySendBlocking(Result.error(error.message,null))
            }
        }
        query.addValueEventListener(queryListener)
        awaitClose{
            query.removeEventListener(queryListener)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun deleteTeam(team: PokemonTeam){
        pokemonDatabase.child(team.id).removeValue().await()
    }

    suspend fun updateTeam(team: PokemonTeam){
        pokemonDatabase.child(team.id).setValue(team).await()
    }

    companion object{
        const val TAG = "UserRepository"
    }

}