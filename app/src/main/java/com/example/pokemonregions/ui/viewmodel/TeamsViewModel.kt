package com.example.pokemonregions.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.pokemonregions.data.repositories.UserRepository
import com.example.pokemonregions.core.Result
import com.example.pokemonregions.data.model.PokemonTeam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel(){

    val teams = MutableStateFlow<Result<List<PokemonTeam?>>>(Result.loading())



    fun getTeams(){
        viewModelScope.launch {
            userRepository.fetchTeams().collect{
                teams.emit(it)
            }
        }
    }
}