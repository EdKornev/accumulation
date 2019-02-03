package com.edkornev.accumulation.api

class CurrencyResponse(
    val base: String,
    val date: String,
    val rates: RateResponse
)