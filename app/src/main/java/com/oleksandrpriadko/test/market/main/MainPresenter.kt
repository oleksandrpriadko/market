package com.oleksandrpriadko.test.market.main

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import com.oleksandrpriadko.test.market.BasePresenter
import com.oleksandrpriadko.test.market.model.BitcoinTickResponse

/**
 * Copyright © 2020 Oleksandr Priadko. All rights reserved.
 * Created Oleksandr Priadko
 */
class MainPresenter : BasePresenter<PresenterView> {

    private var repo: MainRepo? = null

    @VisibleForTesting
    constructor(view: PresenterView) : super(view, false)

    constructor(view: PresenterView, context: Context, baseUrl: String) : super(view, true) {
        repo = MainRepo(view, context, baseUrl)
    }

    @VisibleForTesting
    var currentMode: Mode = Mode.BUY

    @VisibleForTesting
    var currentInputMode: InputMode = InputMode.UNITS

    private var oldSellPrice: Double? = null

    @VisibleForTesting
    var currentSellPrice: Double? = null
    private var oldBuyPrice: Double? = null

    @VisibleForTesting
    var currentBuyPrice: Double? = null

    fun requestViewState() {
        view?.onApplyModeToBuyTitle(currentMode)
        view?.onApplyModeToSellTitle(currentMode)

        view?.onApplyModeToBuyPrice(currentMode)
        view?.onApplyModeToSellPrice(currentMode)

        view?.onApplyModeToBuyPriceIndicator(currentMode)
        view?.onApplyModeToSellPriceIndicator(currentMode)

        view?.onApplyModeToOldBuyPrice(currentMode)
        view?.onApplyModeToOldSellPrice(currentMode)

        view?.onApplyModeUnitsTitle(currentMode)
        view?.onApplyModeAmountTitle(currentMode)

        view?.onApplyModeUnitsInput(currentMode)
        view?.onApplyModeAmountInput(currentMode)

        view?.onApplyModeConfirmButton(currentMode)

        applyPriceChangeToView(currentBuyPrice, currentSellPrice)

        repo?.update(loadingListener)
    }

    fun requestChangeMode(mode: Mode) {
        currentMode = mode

        requestViewState()
    }

    fun unitsChanged(unitsAsString: String) {
        currentInputMode = InputMode.UNITS

        val units: Double? = unitsAsString.toDoubleOrNull()
        val price: Double? = if (currentMode == Mode.BUY) currentBuyPrice else currentSellPrice
        if ((units != null && price != null)) {
            view?.onSetAmount(String.format("%.2f", units * price))
        }

        val isInputNullOrBlank = view?.isInputNullOrBlank() ?: true
        view?.onEnableConfirmButton(!isInputNullOrBlank)
    }

    fun amountChanged(amountAsString: String) {
        currentInputMode = InputMode.AMOUNT

        val amount: Double? = amountAsString.toDoubleOrNull()
        val price: Double? = if (currentMode == Mode.BUY) currentBuyPrice else currentSellPrice
        if (amount != null && price != null) {
            view?.onSetUnits(String.format("%.6f", amount / price))
        }

        val isInputNullOrBlank = view?.isInputNullOrBlank() ?: true
        view?.onEnableConfirmButton(!isInputNullOrBlank)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        consumeOnPendingActionRunnable()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        repo?.abandonTick()
    }

    private fun applyPriceChangeToView(updatedBuyPrice: Double?, updatedSellPrice: Double?) {
        if (updatedBuyPrice == null || updatedSellPrice == null) {
            return
        }

        this@MainPresenter.view?.onSetBuyPrice(updatedBuyPrice)
        this@MainPresenter.view?.onSetSellPrice(updatedSellPrice)

        if (currentBuyPrice == null) {
            currentBuyPrice = updatedBuyPrice
        }

        if (oldBuyPrice != null) {
            if (updatedBuyPrice != (oldBuyPrice ?: 0.0)) {
                val diff: Double = updatedBuyPrice - (oldBuyPrice ?: 0.0)
                when {
                    diff > 0.0 -> this@MainPresenter.view?.onIncreaseBuyIndicator()
                    diff < 0.0 -> this@MainPresenter.view?.onDecreaseBuyIndicator()
                    else -> this@MainPresenter.view?.onSameBuyIndicator()
                }
            }
            this@MainPresenter.view?.onSetOldBuyPrice(currentBuyPrice ?: 0.0)
        } else {
            oldBuyPrice = updatedBuyPrice
            this@MainPresenter.view?.onSetOldBuyPrice(oldBuyPrice ?: 0.0)
        }
        currentBuyPrice = updatedBuyPrice


        if (currentSellPrice == null) {
            currentSellPrice = updatedBuyPrice
        }

        if (oldSellPrice != null) {
            if (updatedSellPrice != (oldSellPrice ?: 0.0)) {
                val diff: Double = updatedSellPrice - (oldSellPrice ?: 0.0)
                when {
                    diff > 0.0 -> this@MainPresenter.view?.onIncreaseSellIndicator()
                    diff < 0.0 -> this@MainPresenter.view?.onDecreaseSellIndicator()
                    else -> this@MainPresenter.view?.onSameSellIndicator()
                }
            }
            this@MainPresenter.view?.onSetOldSellPrice(currentSellPrice ?: 0.0)
        } else {
            oldSellPrice = updatedSellPrice
            this@MainPresenter.view?.onSetOldSellPrice(oldSellPrice ?: 0.0)
        }
        currentSellPrice = updatedSellPrice

        applyPriceChangeToInputs()
    }

    @VisibleForTesting
    fun applyPriceChangeToInputs() {
        when (currentInputMode) {
            InputMode.UNITS -> {
                unitsChanged(view?.getCurrentUnitsAsString() ?: "")
            }
            InputMode.AMOUNT -> {
                amountChanged(view?.getCurrentAmountAsString() ?: "")
            }
        }
    }

    private val loadingListener = object : LoadingListener {
        override fun onTick(response: BitcoinTickResponse) {
            applyPriceChangeToView(updatedBuyPrice = response.gbp.buy,
                    updatedSellPrice = response.gbp.sell)
        }

        override fun onError() {
            this@MainPresenter.view?.showError()
        }
    }
}

interface PresenterView : LifecycleOwner {
    fun onApplyModeToBuyTitle(mode: Mode)
    fun onApplyModeToSellTitle(mode: Mode)

    fun onApplyModeToBuyPriceIndicator(mode: Mode)
    fun onApplyModeToSellPriceIndicator(mode: Mode)

    fun onApplyModeToBuyPrice(mode: Mode)
    fun onApplyModeToSellPrice(mode: Mode)

    fun onApplyModeToOldBuyPrice(mode: Mode)
    fun onApplyModeToOldSellPrice(mode: Mode)

    fun onApplyModeUnitsTitle(mode: Mode)
    fun onApplyModeAmountTitle(mode: Mode)

    fun onApplyModeUnitsInput(mode: Mode)
    fun onApplyModeAmountInput(mode: Mode)

    fun onApplyModeConfirmButton(mode: Mode)

    fun onIncreaseBuyIndicator()
    fun onSameBuyIndicator()
    fun onDecreaseBuyIndicator()

    fun onIncreaseSellIndicator()
    fun onSameSellIndicator()
    fun onDecreaseSellIndicator()

    fun onSetSellPrice(price: Double)
    fun onSetOldSellPrice(price: Double)

    fun onSetBuyPrice(price: Double)
    fun onSetOldBuyPrice(price: Double)

    fun onSetAmount(amountAsString: String)
    fun onSetUnits(unitsAsString: String)

    fun getCurrentUnitsAsString(): String
    fun getCurrentAmountAsString(): String

    fun onEnableConfirmButton(enable: Boolean)

    fun isInputNullOrBlank(): Boolean

    fun showError()
}

enum class Mode {
    BUY, SELL
}

enum class InputMode {
    UNITS, AMOUNT
}