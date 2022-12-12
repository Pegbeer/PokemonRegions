package com.example.pokemonregions.utils

fun String?.capitalize():String?{
    if(this == null || this.isEmpty()) {
        return this
    }
    return this.substring(0, 1).uppercase() + this.substring(1);
}

fun String?.decapitalize():String?{
    if(this == null || this.isEmpty()) {
        return this
    }
    return this.substring(0, 1).lowercase() + this.substring(1);
}

/**
 * Returns a single list of all elements from all collections in the given collection.
 */
fun <T> Iterable<Iterable<T>>.flatten(): List<T> {
    val result = ArrayList<T>()
    for (element in this) {
        result.addAll(element)
    }
    return result
}