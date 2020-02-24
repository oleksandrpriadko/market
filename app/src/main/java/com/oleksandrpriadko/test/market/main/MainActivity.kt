package com.oleksandrpriadko.test.market.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.oleksandrpriadko.test.market.InputFilterDecimalTrim
import com.oleksandrpriadko.test.market.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Copyright © 2020 Oleksandr Priadko. All rights reserved.
 * Created Oleksandr Priadko
 */

class MainActivity : AppCompatActivity(), PresenterView {

    private var presenter: MainPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this, this, getString(R.string.baseUrl))

        presenter?.saveOnPendingActionRunnable(Runnable {
            presenter?.requestViewState()
        })

        val buyClickListener = View.OnClickListener { presenter?.requestChangeMode(Mode.BUY) }
        val sellClickListener = View.OnClickListener { presenter?.requestChangeMode(Mode.SELL) }

        buyTitleTextView.setOnClickListener(buyClickListener)
        buyPriceTextView.setOnClickListener(buyClickListener)
        buyOldPriceTextView.setOnClickListener(buyClickListener)

        sellTitleTextView.setOnClickListener(sellClickListener)
        sellPriceTextView.setOnClickListener(sellClickListener)
        sellOldPriceTextView.setOnClickListener(sellClickListener)

        unitsInput.addTextChangedListener(watcherUnits)
        unitsInput.filters += InputFilterDecimalTrim()

        amountInput.addTextChangedListener(watcherAmount)
        amountInput.filters += InputFilterDecimalTrim()
    }

    override fun onApplyModeToBuyTitle(mode: Mode) {
        when (mode) {
            Mode.BUY -> buyTitleTextView.isSelected = true
            Mode.SELL -> buyTitleTextView.isSelected = false
        }
    }

    override fun onApplyModeToSellTitle(mode: Mode) {
        when (mode) {
            Mode.BUY -> sellTitleTextView.isSelected = false
            Mode.SELL -> sellTitleTextView.isSelected = true
        }
    }

    override fun onApplyModeToBuyPriceIndicator(mode: Mode) {
        when (mode) {
            Mode.BUY -> buyPriceIndicatorImageView.isSelected = true
            Mode.SELL -> buyPriceIndicatorImageView.isSelected = false
        }
    }

    override fun onApplyModeToSellPriceIndicator(mode: Mode) {
        when (mode) {
            Mode.BUY -> sellPriceIndicatorImageView.isSelected = false
            Mode.SELL -> sellPriceIndicatorImageView.isSelected = true
        }
    }

    override fun onApplyModeToBuyPrice(mode: Mode) {
        when (mode) {
            Mode.BUY -> buyPriceTextView.isSelected = true
            Mode.SELL -> buyPriceTextView.isSelected = false
        }
    }

    override fun onApplyModeToSellPrice(mode: Mode) {
        when (mode) {
            Mode.BUY -> sellPriceTextView.isSelected = false
            Mode.SELL -> sellPriceTextView.isSelected = true
        }
    }

    override fun onApplyModeToOldBuyPrice(mode: Mode) {
        when (mode) {
            Mode.BUY -> buyOldPriceTextView.isSelected = true
            Mode.SELL -> buyOldPriceTextView.isSelected = false
        }
    }

    override fun onApplyModeToOldSellPrice(mode: Mode) {
        when (mode) {
            Mode.BUY -> sellOldPriceTextView.isSelected = false
            Mode.SELL -> sellOldPriceTextView.isSelected = true
        }
    }

    override fun onApplyModeUnitsTitle(mode: Mode) {
        when (mode) {
            Mode.BUY -> unitsTitleTextView.isSelected = false
            Mode.SELL -> unitsTitleTextView.isSelected = true
        }
    }

    override fun onApplyModeAmountTitle(mode: Mode) {
        when (mode) {
            Mode.BUY -> amountTitleTextView.isSelected = false
            Mode.SELL -> amountTitleTextView.isSelected = true
        }
    }

    override fun onApplyModeUnitsInput(mode: Mode) {
        unitsInput.setBackgroundResource(when (mode) {
            Mode.BUY -> R.drawable.selector_input_buy
            Mode.SELL -> R.drawable.selector_input_sell
        })
    }

    override fun onApplyModeAmountInput(mode: Mode) {
        amountInput.setBackgroundResource(when (mode) {
            Mode.BUY -> R.drawable.selector_input_buy
            Mode.SELL -> R.drawable.selector_input_sell
        })
    }

    override fun onApplyModeConfirmButton(mode: Mode) {
        when (mode) {
            Mode.BUY -> confirmButton.setBackgroundResource(R.drawable.selector_button_buy)
            Mode.SELL -> confirmButton.setBackgroundResource(R.drawable.selector_button_sell)
        }
    }

    override fun onIncreaseBuyIndicator() {
        buyPriceIndicatorImageView.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp)
        buyPriceIndicatorImageView.imageTintList = getColorStateList(android.R.color.holo_green_dark)
    }

    override fun onSameBuyIndicator() {
        buyPriceIndicatorImageView.setImageResource(R.drawable.ic_remove_black_24dp)
        buyPriceIndicatorImageView.imageTintList = getColorStateList(android.R.color.white)
    }

    override fun onDecreaseBuyIndicator() {
        buyPriceIndicatorImageView.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp)
        buyPriceIndicatorImageView.imageTintList = getColorStateList(android.R.color.holo_red_dark)
    }

    override fun onIncreaseSellIndicator() {
        sellPriceIndicatorImageView.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp)
        sellPriceIndicatorImageView.imageTintList = getColorStateList(android.R.color.holo_green_dark)
    }

    override fun onSameSellIndicator() {
        sellPriceIndicatorImageView.setImageResource(R.drawable.ic_remove_black_24dp)
        sellPriceIndicatorImageView.imageTintList = getColorStateList(android.R.color.white)
    }

    override fun onDecreaseSellIndicator() {
        sellPriceIndicatorImageView.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp)
        sellPriceIndicatorImageView.imageTintList = getColorStateList(android.R.color.holo_red_dark)
    }

    override fun onSetSellPrice(price: Double) {
        sellPriceTextView.text = price.toString()
    }

    override fun onSetOldSellPrice(price: Double) {
        buyOldPriceTextView.text = price.toString()
    }

    override fun onSetBuyPrice(price: Double) {
        buyPriceTextView.text = price.toString()
    }

    override fun onSetOldBuyPrice(price: Double) {
        sellOldPriceTextView.text = price.toString()
    }

    override fun onSetAmount(amountAsString: String) {
        amountInput.removeTextChangedListener(watcherAmount)
        amountInput.setText(amountAsString)
        amountInput.addTextChangedListener(watcherAmount)
    }

    override fun onSetUnits(unitsAsString: String) {
        unitsInput.removeTextChangedListener(watcherUnits)
        unitsInput.setText(unitsAsString)
        unitsInput.addTextChangedListener(watcherUnits)
    }

    override fun getCurrentUnitsAsString(): String {
        return unitsInput.text.toString()
    }

    override fun getCurrentAmountAsString(): String {
        return amountInput.text.toString()
    }

    override fun onEnableConfirmButton(enable: Boolean) {
        confirmButton.isEnabled = enable
    }

    override fun isInputNullOrBlank(): Boolean {
        return unitsInput.text.isNullOrBlank() && amountInput.text.isNullOrBlank()
    }

    override fun showError() {
        Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()
    }

    private val watcherUnits = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                presenter?.unitsChanged(s.toString())
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private val watcherAmount = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                presenter?.amountChanged(s.toString())
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}
