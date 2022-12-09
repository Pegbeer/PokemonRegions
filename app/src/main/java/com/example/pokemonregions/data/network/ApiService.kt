package com.example.pokemonregions.data.network

import com.example.pokemonregions.data.model.Region
import com.example.pokemonregions.core.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ApiService @Inject constructor(
    private val apiClient: IApiClient
) {


    suspend fun downloadRegions() = flow<Result<List<Region>>>{
        val url = "https://pokeapi.co/api/v2/region"
        val response = apiClient.downloadRegions(url)
        if(response.isSuccessful){
            val data = response.body()?.results ?: emptyList()
            emit(Result.success(data))
        }else{
            emit(Result.error(response.message(),null))
        }
    }.flowOn(Dispatchers.IO)

}