package com.chaitanya.mgrecipe.ui.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaitanya.mgrecipe.ui.detail.DetailRepository
import com.chaitanya.recipedata.local.entity.RecipeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(private val repository: DetailRepository) :
    ViewModel() {

    val allRecipes: LiveData<List<RecipeEntity>> = repository.getFavouriteRecipes()

    fun getRecipeById(id: Long, returnData: (RecipeEntity?) -> Unit) {
        viewModelScope.launch {
            returnData(repository.getFavouriteRecipeById(id))
        }
    }
}