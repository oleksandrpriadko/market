package com.oleksandrpriadko.test.market.main

import android.text.InputFilter
import android.text.Spanned

/**
 * Copyright © 2020 Oleksandr Priadko. All rights reserved.
 * Created Oleksandr Priadko
 */
class InputFilterDecimalTrim : InputFilter {

    override fun filter(source: CharSequence?,
                        start: Int,
                        end: Int,
                        dest: Spanned?,
                        dstart: Int,
                        dend: Int): CharSequence {
        return if (dest != null && dest.contains(".")) {
            val afterDot: Int = dest.length - 1 - dest.indexOf(".")
            val overLimit: Int = afterDot - 2
            if (overLimit >= 0) "" else source ?: ""
        } else {
            source ?: ""
        }
    }
}