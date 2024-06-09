package com.chaitanya.mgrecipe.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaitanya.mgrecipe.utility.NetworkResult
import com.chaitanya.mgrecipe.utility.handleApiCall
import com.chaitanya.recipedata.models.AllProductsResponse
import com.chaitanya.recipedata.models.RandomRecipeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {
    var currentOffset = 0
    var isLoading = false
    var isMaxPageReached = false

    fun fetchPaginatedData() {
        if (isLoading) return

        viewModelScope.launch {
            isLoading = true
            getAllRecipeData(currentOffset)
        }
    }


    private val _allRecipesData = MutableLiveData<NetworkResult<AllProductsResponse>>()
    val allRecipesData: LiveData<NetworkResult<AllProductsResponse>> get() = _allRecipesData

    fun getAllRecipeData(offset: Int) {
        viewModelScope.launch {
            handleApiCall(
                apiCall = { repository.getAllData(offset) },
                responseLiveData = _allRecipesData
            )
        }
    }

    private val _combinedData =
        MutableLiveData<NetworkResult<Pair<AllProductsResponse, RandomRecipeResponse>>>()
    val combinedData: LiveData<NetworkResult<Pair<AllProductsResponse, RandomRecipeResponse>>> get() = _combinedData


    fun fetchCombinedData() {
        viewModelScope.launch {
            try {
                isLoading = true
                _combinedData.postValue(NetworkResult.Loading())

                val popularDataDeferred = async { repository.getPopularData() }
                val allRecipesDataDeferred = async { repository.getAllData() }

                val popularData = popularDataDeferred.await()
                val allRecipesData = allRecipesDataDeferred.await()

                if (popularData.isSuccessful && popularData.body() != null && allRecipesData.isSuccessful && allRecipesData.body() != null) {
                    val combinedResult = Pair(allRecipesData.body()!!, popularData.body()!!)
                    _combinedData.postValue(NetworkResult.Success(combinedResult))
                } else {
                    _combinedData.postValue(NetworkResult.Error(500, "Something Went wrong"))
                }
            } catch (e: Exception) {
                _combinedData.postValue(NetworkResult.Error(500, e.message ?: "An error occurred"))
            }
        }
    }


}