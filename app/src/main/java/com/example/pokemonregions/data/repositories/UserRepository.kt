package com.example.pokemonregions.data.repositories

import com.example.pokemonregions.data.model.User
import com.example.pokemonregions.core.Result
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val database:FirebaseDatabase
) {

    private val databaseUser:DatabaseReference by lazy { database.getReference(User::class.simpleName ?: "User")}

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

}