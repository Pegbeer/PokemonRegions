package com.example.pokemonregions.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.pokemonregions.data.model.Region
import com.example.pokemonregions.data.model.User
import com.example.pokemonregions.data.network.ApiService
import com.example.pokemonregions.data.repositories.UserRepository
import com.example.pokemonregions.core.Result
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val apiService: ApiService
) : BaseViewModel() {

    val user = MutableStateFlow<User?>(null)

    val regions = MutableStateFlow<Result<List<Region>>>(Result.loading())


    suspend fun createUser(authUser:FirebaseUser?){
        if(authUser != null){
            val user = User(
                authUser.uid,
                authUser.displayName ?: "",
                authUser.email ?: "",
                emptyList()
            )

            val result = userRepository.createUser(user)
            this.user.emit(result)
        }
    }

    suspend fun setUserLogged(authUser: FirebaseUser){
        val user = User(
            authUser.uid,
            authUser.displayName ?: "",
            authUser.email ?: "",
            emptyList()
        )

        this.user.emit(user)
    }

    fun downloadRegions(){
        viewModelScope.launch {
            apiService.downloadRegions().collect{
                regions.emit(it)
            }
        }
    }

    fun userExists(authUser: FirebaseUser?) = userRepository.checkUserExists(authUser?.email)


}