package com.edkornev.accumulation.api

import com.google.gson.annotations.SerializedName

class RateResponse(
    @SerializedName("USD")
    val usd: Double,
    @SerializedName("EUR")
    val eur: Double
)