package com.example.pokemonregions.utils

fun String?.capitalize():String?{
    if(this == null || this.isEmpty()) {
        return this
    }
    return this.substring(0, 1).uppercase() + this.substring(1);
}