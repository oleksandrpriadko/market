package com.oleksandrpriadko.test.market.model

import com.google.gson.annotations.Expose

class GBP {
    @Expose
    var buy: Double = 0.0
    @Expose
    var last: Double = 0.0
    @Expose
    var sell: Double = 0.0
    @Expose
    var symbol: String = ""

}