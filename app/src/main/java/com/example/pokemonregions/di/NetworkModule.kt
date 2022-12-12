package com.example.pokemonregions.di


import com.apollographql.apollo3.ApolloClient
import com.example.pokemonregions.data.network.ApiService
import com.example.pokemonregions.data.network.IApiClient
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    const val url:String = "https://pokeapi.co/api/v2/"
    const val apolloUrl:String = "https://beta.pokeapi.co/graphql/v1beta"

    @Provides
    @Singleton
    fun provideDatabase():FirebaseDatabase = Firebase.database


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiClient(retrofit: Retrofit):IApiClient{
        return retrofit.create(IApiClient::class.java)
    }

    @Provides
    @Singleton
    fun provideApolloClient() = ApolloClient.Builder()
        .serverUrl(apolloUrl)
        .build()

    @Provides
    @Singleton
    fun provideApiService(apolloClient: ApolloClient) = ApiService(apolloClient)


}