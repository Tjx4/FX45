package com.platform45.fx45.networking.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object API {
    var retrofit: Services

    init {
        val builder = Retrofit.Builder()
            .baseUrl(Hosts.Production.url)
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()

        API.retrofit = retrofit.create(Services::class.java)
    }
}