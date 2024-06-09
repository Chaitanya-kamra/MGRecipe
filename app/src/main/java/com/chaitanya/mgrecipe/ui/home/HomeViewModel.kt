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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) :ViewModel() {
    private val _popularData = MutableLiveData<NetworkResult<RandomRecipeResponse>>()
    val popularData: LiveData<NetworkResult<RandomRecipeResponse>> get() = _popularData

    fun getPopularData(){
        viewModelScope.launch {
            handleApiCall(
                apiCall = {repository.getPopularData()},
                responseLiveData = _popularData
            )
        }
    }

    private val _allRecipesData = MutableLiveData<NetworkResult<AllProductsResponse>>()
    val allRecipesData: LiveData<NetworkResult<AllProductsResponse>> get() = _allRecipesData

    fun getAllRecipeData(){
        viewModelScope.launch {
            handleApiCall(
                apiCall = {repository.getAllData()},
                responseLiveData = _allRecipesData
            )
        }
    }

}