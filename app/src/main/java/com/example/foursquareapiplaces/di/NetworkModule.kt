package com.example.foursquareapiplaces.di

import com.example.foursquareapiplaces.Network.ApiPlaces.ApiPlaces
import com.example.foursquareapiplaces.Network.ApiPlaces.HeaderInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    //Provide Retrofit
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.foursquare.com/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
            .build()
    }

    private fun getClient():OkHttpClient{
        val  client= OkHttpClient.Builder().addInterceptor(HeaderInterceptor()).build()
        return client
    }


    //Call Retrofit
    @Singleton
    @Provides
    fun provideFqApiClient(retrofit: Retrofit): ApiPlaces {
        return retrofit.create(ApiPlaces::class.java)
    }

}