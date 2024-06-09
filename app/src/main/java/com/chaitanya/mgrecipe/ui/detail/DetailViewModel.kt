package com.chaitanya.mgrecipe.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaitanya.mgrecipe.utility.NetworkResult
import com.chaitanya.mgrecipe.utility.handleApiCall
import com.chaitanya.recipedata.local.RecipeDatabase
import com.chaitanya.recipedata.local.entity.RecipeEntity
import com.chaitanya.recipedata.models.RecipeEquipmentResponse
import com.chaitanya.recipedata.models.SingleRecipeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: DetailRepository) : ViewModel() {

    var recipeData :SingleRecipeResponse? = null
    var recipeEquipment :RecipeEquipmentResponse? = null

    private val _recipeById = MutableLiveData<NetworkResult<SingleRecipeResponse>>()
    val recipeById: LiveData<NetworkResult<SingleRecipeResponse>> get() = _recipeById

    fun getRecipeById(id: String){
        viewModelScope.launch {
            awaitAll(
                async {
                    handleApiCall(
                        apiCall = {repository.getRecipeById(id)},
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
                apiCall = {repository.getRecipeEquipmentById(id)},
                responseLiveData = _recipeEquipmmentById
            )
        }
    }

    fun addToFavorite() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository.addToFourite(
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
    fun addLocalToFavorite(recipeEntity: RecipeEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository.addToFourite(
                    recipeEntity
                )
            }
        }
    }

    fun removeFavourite(id: Long){
        viewModelScope.launch {
            repository.removeFromFourite(id)
        }
    }
    fun isRecipeExist(id: Long,isExist:(Boolean)->Unit) {
        viewModelScope.launch {
            isExist(repository.isRecipeExist(id))
        }
    }
}