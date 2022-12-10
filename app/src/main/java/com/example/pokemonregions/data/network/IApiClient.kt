package com.example.pokemonregions.data.network

import com.example.pokemonregions.data.model.RegionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface IApiClient {

    @GET
    suspend fun downloadRegions(
        @Url url:String
    ):Response<RegionResponse>

}