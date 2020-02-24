package com.oleksandrpriadko.test.market.model

import com.google.gson.annotations.SerializedName

class BitcoinTickResponse {
    @SerializedName("GBP")
    val gbp: GBP = GBP()
}