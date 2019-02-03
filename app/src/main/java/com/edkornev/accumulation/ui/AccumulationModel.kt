package com.edkornev.accumulation.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.edkornev.accumulation.R
import com.edkornev.accumulation.api.CurrencyEndpoint
import com.edkornev.accumulation.api.CurrencyResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_CURRENCY = "RUB"
private const val USD = "USD"
private const val EUR = "EUR"

class AccumulationModel(application: Application) : AndroidViewModel(application) {

    private val currencyEndpoint: CurrencyEndpoint = Retrofit.Builder()
        .baseUrl(application.getString(R.string.api_base_url))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyEndpoint::class.java)

    private var view: AccumulationView? = null

    private var usdRate: Double = 0.0
    private var eurRate: Double = 0.0

    override fun onCleared() {
        this.view = null
        super.onCleared()
    }

    fun attachView(view: AccumulationView) {
        this.view = view

        loadCurrencies()
    }

    fun changeValue(rub: String, usd: String, eur: String) {
        var total = rub.toDouble()

        if (usd.isNotBlank() && usdRate > 0) {
            val usdToRub = usd.toDouble() / usdRate
            total += usdToRub
        }

        if (eur.isNotBlank() && eurRate > 0) {
            val eurToRub = eur.toDouble() / eurRate
            total += eurToRub
        }

        view?.setResult(total.toInt().toString())
    }

    fun loadCurrencies() {
        val symbols = "$USD,$EUR"

        currencyEndpoint.getCurrencies(BASE_CURRENCY, symbols).enqueue(
            object : Callback<CurrencyResponse> {
                override fun onFailure(call: Call<CurrencyResponse>, error: Throwable) {
                    view?.onError()
                }

                override fun onResponse(call: Call<CurrencyResponse>, response: Response<CurrencyResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.also { currencyResponse ->
                            usdRate = currencyResponse.rates.usd
                            eurRate = currencyResponse.rates.eur

                            view?.onLoadedCurrencies(currencyResponse.date)
                        }
                    }
                }
            }
        )
    }

    private fun saveRates() {

    }
}