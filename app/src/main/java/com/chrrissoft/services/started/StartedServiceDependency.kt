package com.chrrissoft.services.started

interface StartedServiceDependency {
    fun startWork(data: String, id: Int)

    fun stopWork()

    fun addOnFinishedListener(listener: OnFinishedListener)

    fun removeOnFinishedListener(listener: OnFinishedListener)

    fun interface OnFinishedListener {
        operator fun invoke()
    }
}
