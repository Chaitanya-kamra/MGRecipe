package com.chaitanya.mgrecipe.utility


sealed class NetworkResult<T>(
    val data: T? = null,
    val code: Int? = null,
    val message: String? = null
) {

    class Success<T>(data: T, code: Int? = 200) : NetworkResult<T>(data)

    class Error<T>(code: Int?, message: String?, data: T? = null) :
        NetworkResult<T>(data, code, message)

    class Loading<T> : NetworkResult<T>()
}