package com.oleksandrpriadko.test.market.model

import retrofit2.Call
import retrofit2.http.GET

/**
 * Copyright © 2020 Oleksandr Priadko. All rights reserved.
 * Created Oleksandr Priadko
 */
interface Api {

    @GET("/ticker")
    fun tick() : Call<BitcoinTickResponse>

}