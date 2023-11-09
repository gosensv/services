package com.chrrissoft.services.shared

sealed interface ResState<out T> {
    object Loading : ResState<Nothing>
    data class Error(val throwable: Throwable?) : ResState<Throwable>
    data class Success<T>(val data: T) : ResState<T>
}
