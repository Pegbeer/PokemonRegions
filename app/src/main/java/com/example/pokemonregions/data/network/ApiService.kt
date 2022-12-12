package com.example.pokemonregions.data.network

import com.apollographql.apollo3.ApolloClient
import com.example.PokemonsQuery
import com.example.RegionsQuery
import com.example.pokemonregions.core.Result
import com.example.pokemonregions.data.model.Generation
import com.example.pokemonregions.data.model.Pokemon
import com.example.pokemonregions.data.model.PokemonType
import com.example.pokemonregions.data.model.Region
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ApiService @Inject constructor(
    private val apolloClient: ApolloClient
) {


    fun queryRegions() = flow {
        emit(Result.loading())
        val response = apolloClient.query(RegionsQuery()).execute()
        val result = response.data?.pokemon_v2_region

        if(response.hasErrors()){
            val error = response.errors?.get(0)
            emit(Result.error(error?.message ?: "Error trying to download regions",null))
        }

        if(result == null){
            emit(Result.success(emptyList()))
        }else{
            val data = result.map {
                val gen = if(it.generation.isNotEmpty()){
                    it.generation[0]
                }else{
                    null
                }
                Region(
                    it.id,
                    it.name,
                    Generation(
                        gen?.id ?: 0,
                        gen?.name ?: ""
                    )
                )
            }
            emit(Result.success(data))
        }
    }.flowOn(Dispatchers.IO)


    fun queryPokemons(generation:Generation) = flow {
        emit(Result.loading())
        val response = apolloClient.query(PokemonsQuery(generation.name)).execute()
        val result = response.data?.species

        if(response.hasErrors()){
            val error = response.errors?.get(0)
            emit(Result.error(error?.message ?: "Error trying to download regions",null))
        }

        if(result == null){
            emit(Result.success(emptyList()))
        }else{
            val data = result.asSequence().map {
                it.pokemons
            }.flatten().map {
              Pokemon(
                  it.id,
                  it.name,
                  it.types.map {t ->
                      PokemonType(t.type?.name)
                  }
              )
            }
            emit(Result.success(data.toList()))
        }
    }.flowOn(Dispatchers.IO)

}