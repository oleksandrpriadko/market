package com.oleksandrpriadko.test.market.main

import androidx.lifecycle.LifecycleOwner
import com.oleksandrpriadko.test.market.BasePresenter

/**
 * Copyright © 2020 Oleksandr Priadko. All rights reserved.
 * Created Oleksandr Priadko
 */
class MainPresenter(view: PresenterView) : BasePresenter<PresenterView>(view) {

    private val repo: MainRepo = MainRepo(view)

    private var currentMode: Mode = Mode.BUY

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


        val isInputNullOrBlank = view?.isInputNullOrBlank() ?: true
        view?.onEnableConfirmButton(!isInputNullOrBlank)
    }

    fun requestChangeMode(mode: Mode) {
        currentMode = mode

        requestViewState()
    }

    fun unitsChanged(unitsAsString: String) {

    }

    fun amountChanged(amountAsString: String) {

    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        consumeOnPendingActionRunnable()
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
    fun onIncreaseSellIndicator()

    fun onDecreaseBuyIndicator()
    fun onDecreaseSellIndicator()

    fun onEnableConfirmButton(enable: Boolean)

    fun isInputNullOrBlank(): Boolean
}

enum class Mode {
    BUY, SELL
}