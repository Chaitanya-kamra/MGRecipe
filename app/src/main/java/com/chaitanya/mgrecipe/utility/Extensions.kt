package com.chaitanya.mgrecipe.utility

import android.view.View
import android.widget.ImageView
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException


fun View.gone(){
    this.visibility = View.GONE
}
fun View.visible(){
    this.visibility = View.VISIBLE
}

suspend fun <T> handleApiCall(
    apiCall: suspend () -> Response<T>,
    responseLiveData: MutableLiveData<NetworkResult<T>>
) {
    responseLiveData.postValue(NetworkResult.Loading())
    try {
        val response = apiCall.invoke()
        if (response.isSuccessful && response.body() != null && response.code() in 200..299) {
            responseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                val statusCode = errorObj.getInt("statusCode")
                val errorMessage = errorObj.getString("message")
                responseLiveData.postValue(NetworkResult.Error(statusCode, errorMessage))
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                responseLiveData.postValue(NetworkResult.Error(500, "Something Went Wrong"))
            }
        } else {
            responseLiveData.postValue(NetworkResult.Error(500, "Something Went Wrong"))
        }
    } catch (t: Throwable) {
        t.printStackTrace()
        when (t) {
            is CancellationException -> responseLiveData.postValue(
                NetworkResult.Error(
                    500,
                    ""
                )
            )

            is IOException -> responseLiveData.postValue(
                NetworkResult.Error(
                    500,
                    "Something Went Wrong"
                )
            )

            else -> responseLiveData.postValue(NetworkResult.Error(500, "Something Went Wrong"))
        }
    }
}

fun ImageView.loadImage(url: String?, placeholderResId: Int? = null) {
    val glideRequest = Glide.with(context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)

    placeholderResId?.let {
        glideRequest.placeholder(it)
    }
    glideRequest.into(this)
}

val View.IskeyboardVisible: Boolean
    get() = WindowInsetsCompat
        .toWindowInsetsCompat(rootWindowInsets)
        .isVisible(WindowInsetsCompat.Type.ime())