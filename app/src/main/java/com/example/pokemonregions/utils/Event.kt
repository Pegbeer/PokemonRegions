package com.example.pokemonregions.utils

open class Event<out T> (private val content:T){
    var handled = false

    fun getContentIfNotHandled(): T?{
        return if (handled){
            null
        }
        else{
            handled = true
            content
        }
    }
}