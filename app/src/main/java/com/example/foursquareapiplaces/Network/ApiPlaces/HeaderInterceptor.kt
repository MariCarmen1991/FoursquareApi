package com.example.foursquareapiplaces.Network.ApiPlaces

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request= chain.request().newBuilder().addHeader(
            "Accept", "application/json"
        ).addHeader(
            "Authorization", "fsq3KbkZm2+2hQCoZOndPxxMFMoLKVdXRu2IAo3ISw2v/9I="
        ).build()

        return chain.proceed(request)
    }


}