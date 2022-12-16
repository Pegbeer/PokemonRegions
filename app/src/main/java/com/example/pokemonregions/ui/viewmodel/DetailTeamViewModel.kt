package com.example.pokemonregions.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.pokemonregions.data.model.PokemonTeam
import com.example.pokemonregions.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailTeamViewModel @Inject constructor(
    private val repository: UserRepository
) : BaseViewModel() {


    fun deleteTeam(team:PokemonTeam){
        viewModelScope.launch {
            repository.deleteTeam(team)
        }
    }

    fun updateTeam(team: PokemonTeam){
        viewModelScope.launch {
            repository.updateTeam(team)
        }
    }
}