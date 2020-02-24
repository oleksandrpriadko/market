package com.oleksandrpriadko.test.market.main

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.runners.MockitoJUnitRunner

/**
 * Copyright © 2020 Oleksandr Priadko. All rights reserved.
 * Created Oleksandr Priadko
 */
@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    @Mock
    private lateinit var viewMock: PresenterView

    private lateinit var presenter: MainPresenter

    @Before
    fun setUp() {
        presenter = MainPresenter(viewMock)
    }

    @Test
    fun givenUnitsBuy_whenApplyPriceChangeToInputs() {
        presenter.currentInputMode = InputMode.UNITS
        presenter.currentMode = Mode.BUY
        presenter.currentBuyPrice = 3456.00

        `when`(viewMock.getCurrentUnitsAsString()).thenReturn("4")

        presenter.applyPriceChangeToInputs()

        verify(viewMock).onSetAmount("13824.00")
        verify(viewMock).isInputNullOrBlank()
        verify(viewMock).onEnableConfirmButton(true)
    }

    @Test
    fun givenAmountSell_whenApplyPriceChangeToInputs() {
        presenter.currentInputMode = InputMode.AMOUNT
        presenter.currentMode = Mode.SELL
        presenter.currentSellPrice = 13456.00

        `when`(viewMock.getCurrentAmountAsString()).thenReturn("4324")

        presenter.applyPriceChangeToInputs()

        verify(viewMock).onSetUnits("0.321344")
        verify(viewMock).isInputNullOrBlank()
        verify(viewMock).onEnableConfirmButton(true)
    }
}