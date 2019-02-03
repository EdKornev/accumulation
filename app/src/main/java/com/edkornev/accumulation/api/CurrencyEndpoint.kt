package com.edkornev.accumulation.api

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface CurrencyEndpoint {

    @POST(value = "latest")
    fun getCurrencies(@Query("base") base: String, @Query(value = "symbols") symbols: String): Call<CurrencyResponse>
}