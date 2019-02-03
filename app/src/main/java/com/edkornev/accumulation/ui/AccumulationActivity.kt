package com.edkornev.accumulation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.edkornev.accumulation.R

interface AccumulationView {
    fun setResult(result: String)
    fun onLoadedCurrencies(date: String)
    fun onError()
}

class AccumulationActivity : AppCompatActivity(),
    AccumulationView {

    private lateinit var model: AccumulationModel

    private lateinit var etRub: EditText
    private lateinit var etUsd: EditText
    private lateinit var etEur: EditText
    private lateinit var tvResult: TextView
    private lateinit var tvRates: TextView

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            model.changeValue(
                rub = etRub.text.toString(),
                usd = etUsd.text.toString(),
                eur = etEur.text.toString()
            )
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // empty
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // empty)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = ViewModelProvider.AndroidViewModelFactory(application)
            .create(AccumulationModel::class.java)

        model.attachView(this)

        initViews()
    }

    // region AccumulationView
    override fun setResult(result: String) {
        tvResult.text = getString(R.string.tv_result, result)
    }

    override fun onLoadedCurrencies(date: String) {
        tvRates.text = getString(R.string.tv_rates, date)
    }

    override fun onError() {
        AlertDialog.Builder(applicationContext)
            .setTitle(R.string.dialog_error_title)
            .setMessage(R.string.dialog_error_api_message)
            .create()
            .show()
    }
    // endregion

    private fun initViews() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle(R.string.app_name)

        etRub = findViewById(R.id.et_rub)
        etRub.addTextChangedListener(textWatcher)
        etUsd = findViewById(R.id.et_usd)
        etUsd.addTextChangedListener(textWatcher)
        etEur = findViewById(R.id.et_eur)
        etEur.addTextChangedListener(textWatcher)

        tvResult = findViewById(R.id.tv_result)
        tvRates = findViewById(R.id.tv_rates)

        findViewById<View>(R.id.iv_refresh).setOnClickListener {
            model.loadCurrencies()
        }
    }
}
