package com.example.pokemonregions

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PokemonRegions : Application(){
    companion object{
        const val TAG = "application"
    }
}