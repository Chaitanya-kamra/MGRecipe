package com.chaitanya.mgrecipe.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaitanya.mgrecipe.ui.detail.DetailRepository
import com.chaitanya.mgrecipe.utility.NetworkResult
import com.chaitanya.mgrecipe.utility.handleApiCall
import com.chaitanya.recipedata.local.RecipeDatabase
import com.chaitanya.recipedata.local.entity.RecipeEntity
import com.chaitanya.recipedata.models.RecipeEquipmentResponse
import com.chaitanya.recipedata.models.SearchQueryResponseItem
import com.chaitanya.recipedata.models.SimilarRecipeResponseItem
import com.chaitanya.recipedata.models.SingleRecipeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository ,private val detailRepository: DetailRepository) : ViewModel() {

    var recipeData :SingleRecipeResponse? = null
    var recipeEquipment :RecipeEquipmentResponse? = null

    private val _searchQueryResponse = MutableLiveData<NetworkResult<ArrayList<SearchQueryResponseItem>>>()
    val searchQueryResponse: LiveData<NetworkResult<ArrayList<SearchQueryResponseItem>>> get() = _searchQueryResponse
    var searchJob : Job? = null
    fun getsearchQueryResponse(query: String){
        searchJob?.cancel()
        searchJob = null
        searchJob = viewModelScope.launch {
            handleApiCall(
                apiCall = {repository.getSearchReponse(query)},
                responseLiveData = _searchQueryResponse
            )
        }
    }
    private val _recipeById = MutableLiveData<NetworkResult<SingleRecipeResponse>>()
    val recipeById: LiveData<NetworkResult<SingleRecipeResponse>> get() = _recipeById

    fun getRecipeById(id: String){
        viewModelScope.launch {
            awaitAll(
                async {
                    handleApiCall(
                        apiCall = {detailRepository.getRecipeById(id)},
                        responseLiveData = _recipeById
                    )
                },async { getRecipeEquipmmentById(id) } )

        }
    }
    private val _recipeEquipmmentById = MutableLiveData<NetworkResult<RecipeEquipmentResponse>>()
    val recipeEquipmmentById: LiveData<NetworkResult<RecipeEquipmentResponse>> get() = _recipeEquipmmentById

    fun getRecipeEquipmmentById(id: String){
        viewModelScope.launch {
            handleApiCall(
                apiCall = {detailRepository.getRecipeEquipmentById(id)},
                responseLiveData = _recipeEquipmmentById
            )
        }
    }

    private val _recipeSimilarById = MutableLiveData<NetworkResult<ArrayList<SimilarRecipeResponseItem>>>()
    val recipeSimilarById: LiveData<NetworkResult<ArrayList<SimilarRecipeResponseItem>>> get() = _recipeSimilarById
    fun getSimilarRecipe(id: String) {
        viewModelScope.launch {
            handleApiCall(
                apiCall = {repository.getSimilarRecipe(id)},
                responseLiveData = _recipeSimilarById
            )
        }
    }
    fun addToFavorite() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                detailRepository.addToFourite(
                    RecipeEntity(
                        id = recipeData!!.id,
                        title = recipeData?.title,
                        readyInMinutes = recipeData?.readyInMinutes,
                        servings = recipeData?.servings,
                        sourceUrl = recipeData?.sourceUrl,
                        image = recipeData?.image,
                        imageType = recipeData?.imageType,
                        nutrition = recipeData?.nutrition,
                        summary = recipeData?.summary,
                        instructions = recipeData?.instructions,
                        pricePerServing = recipeData?.pricePerServing,
                        extendedIngredients = recipeData?.extendedIngredients,
                        equipment = recipeEquipment?.equipment
                    )
                )
            }
        }
    }
    fun removeFavourite(id: Long){
        viewModelScope.launch {
            detailRepository.removeFromFourite(id)
        }
    }
    fun isRecipeExist(id: Long,isExist:(Boolean)->Unit) {
        viewModelScope.launch {
            isExist(detailRepository.isRecipeExist(id))
        }
    }
}