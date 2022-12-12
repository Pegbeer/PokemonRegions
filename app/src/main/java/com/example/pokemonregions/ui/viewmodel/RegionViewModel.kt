package com.example.pokemonregions.ui.viewmodel

import androidx.lifecycle.*
import com.example.pokemonregions.core.Result
import com.example.pokemonregions.data.model.Generation
import com.example.pokemonregions.data.model.Pokemon
import com.example.pokemonregions.data.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegionViewModel @Inject constructor(
    private val apiService: ApiService
) : BaseViewModel() {

    private val limitByTeam = 6

    val pokemons = MutableStateFlow<Result<List<Pokemon>>>(Result.loading())

    private val _pokemonsSelected = mutableListOf<Pokemon>()

    private val _pokemonsSelectedState = MutableSharedFlow<MutableList<Pokemon>>()
    val pokemonsSelectedState get() = _pokemonsSelectedState.asSharedFlow()

    fun getPokemons(generation: Generation) {
        viewModelScope.launch {
            apiService.queryPokemons(generation).collect{
                pokemons.emit(it)
            }
        }
    }

    fun cannotAddPokemon() = _pokemonsSelected.size == limitByTeam

    fun selectPokemon(pokemon: Pokemon){
        viewModelScope.launch {
            _pokemonsSelected.add(pokemon)
            _pokemonsSelectedState.emit(_pokemonsSelected)
        }
    }

}