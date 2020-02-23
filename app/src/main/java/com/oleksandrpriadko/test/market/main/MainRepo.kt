package com.oleksandrpriadko.test.market.main

import androidx.lifecycle.LifecycleOwner
import com.android.oleksandrpriadko.mvp.repo.ObservableRepo

/**
 * Copyright © 2020 Oleksandr Priadko. All rights reserved.
 * Created Oleksandr Priadko
 */
class MainRepo(view: LifecycleOwner) : ObservableRepo(view) {



    override fun cleanUp() {

    }
}