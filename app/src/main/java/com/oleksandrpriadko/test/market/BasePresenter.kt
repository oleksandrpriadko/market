package com.oleksandrpriadko.test.market

import androidx.annotation.CallSuper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * Copyright © 2020 Oleksandr Priadko. All rights reserved.
 * Created By Oleksandr Priadko
 */
abstract class BasePresenter<T : LifecycleOwner>(view: T) : DefaultLifecycleObserver {

    private val runnableOnNewIntentList: MutableList<Runnable> = mutableListOf()
    private val runnableOnActivityResultList: MutableList<Runnable> = mutableListOf()
    private val runnableOnPendingActionList: MutableList<Runnable> = mutableListOf()

    private var isViewBound: Boolean = false

    protected var view: T? = view
        get() {
            field?.let {
                return when {
                    it.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED) && this.isViewBound
                            || this.isViewInEditMode -> it
                    else -> null
                }
            }
            return null
        }

    init {
        subscribeView(view)
    }

    private fun subscribeView(view: T) {
        if (view.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            logState("dead lifecycle owner")
        } else {
            view.lifecycle.addObserver(this)
            this.view = view
            logState("subscribed")
        }
    }

    @CallSuper
    override fun onCreate(owner: LifecycleOwner) {
        bindView(owner)
    }

    @CallSuper
    override fun onResume(owner: LifecycleOwner) {
        bindView(owner)
    }

    @CallSuper
    private fun bindView(owner: LifecycleOwner) {
        this.isViewBound = true
        this.logState(owner.lifecycle.currentState.name)
    }

    @CallSuper
    override fun onStop(owner: LifecycleOwner) {
        this.logState(owner.lifecycle.currentState.name)
        this.isViewBound = false
    }

    @CallSuper
    override fun onDestroy(owner: LifecycleOwner) {
        this.logState(owner.lifecycle.currentState.name)
        this.isViewBound = false
        this.view = null
    }

    protected fun enableLog(): Boolean = false

    protected fun logState(message: String) {
        if (enableLog()) {
            // Timber or Log
        }
    }

    fun saveOnNewIntentRunnable(runnable: Runnable, executeIfBound: Boolean = true) {
        saveRunnableToList(runnable, runnableOnNewIntentList, executeIfBound)
    }

    protected fun consumeOnNewIntentRunnable() {
        consumeRunnableList(runnableOnNewIntentList)
    }

    fun saveOnActivityResultRunnable(runnable: Runnable, executeIfBound: Boolean = true) {
        saveRunnableToList(runnable, runnableOnActivityResultList, executeIfBound)
    }

    protected fun consumeOnActivityResultRunnable() {
        consumeRunnableList(runnableOnActivityResultList)
    }

    fun saveOnPendingActionRunnable(runnable: Runnable, executeIfBound: Boolean = true) {
        saveRunnableToList(runnable, runnableOnPendingActionList, executeIfBound)
    }

    private fun saveRunnableToList(runnable: Runnable,
                                   runnableList: MutableList<Runnable>,
                                   executeIfBound: Boolean) {
        if (view != null && executeIfBound) {
            runnable.run()
        } else {
            runnableList.add(runnable)
        }
    }

    protected fun consumeOnPendingActionRunnable() {
        consumeRunnableList(runnableOnPendingActionList)
    }

    private fun consumeRunnableList(runnableList: MutableList<Runnable>) {
        while (runnableList.iterator().hasNext()) {
            val runnable = runnableList.iterator().next()
            runnable.run()
            runnableList.remove(runnable)
        }
    }

    open var isViewInEditMode = false
}