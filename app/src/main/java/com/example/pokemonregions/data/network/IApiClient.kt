package com.example.pokemonregions.data.network

import com.example.pokemonregions.data.model.Region
import com.example.pokemonregions.data.model.RegionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface IApiClient {

    @GET("region")
    suspend fun downloadRegions():Response<RegionResponse>

    @GET("region/{name}")
    suspend fun getRegionById(
        @Path("name")name:String
    ):Response<Region>

}