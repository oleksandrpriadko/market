package com.oleksandrpriadko.test.market.main

import android.content.Context
import android.os.Handler
import androidx.lifecycle.LifecycleOwner
import com.android.oleksandrpriadko.mvp.repo.ObservableRepo
import com.android.oleksandrpriadko.mvp.repo_extension.RetrofitRepoExtension
import com.oleksandrpriadko.test.market.model.Api
import com.oleksandrpriadko.test.market.model.BitcoinTickResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Copyright © 2020 Oleksandr Priadko. All rights reserved.
 * Created Oleksandr Priadko
 */
class MainRepo(view: LifecycleOwner, context: Context, baseUrl: String) : ObservableRepo(view) {

    private val retrofit: RetrofitRepoExtension = RetrofitRepoExtension(
            context = context,
            baseUrl = baseUrl,
            converterFactory = GsonConverterFactory.create())

    private var listener: LoadingListener? = null

    private val api: Api? = retrofit.getApi(Api::class.java)

    private val tickHandler: Handler = Handler()
    private val runnableTick = object : Runnable {
        override fun run() {
            requestBitcoinUpdate()
            tickHandler.postDelayed(this, TimeUnit.SECONDS.toMillis(15))
        }

    }

    fun update(listener: LoadingListener?) {
        abandonTick()

        this.listener = listener
        tickHandler.post(runnableTick)
    }

    private fun requestBitcoinUpdate() {
        api?.tick()?.enqueue(object : Callback<BitcoinTickResponse> {
            override fun onResponse(call: Call<BitcoinTickResponse>, response: Response<BitcoinTickResponse>) {
                if (response.isSuccessful) {
                    val body: BitcoinTickResponse? = response.body()
                    if (body != null) {
                        listener?.onTick(body)
                    } else {
                        listener?.onError()
                    }
                }
            }

            override fun onFailure(call: Call<BitcoinTickResponse>, t: Throwable) {
                listener?.onError()
            }

        })
    }

    fun abandonTick() {
        listener = null
        tickHandler.removeCallbacksAndMessages(null)
    }

    override fun cleanUp() {
        abandonTick()
    }
}

interface LoadingListener {
    fun onTick(response: BitcoinTickResponse)
    fun onError()
}