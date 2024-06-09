package com.chaitanya.mgrecipe.ui.home

import com.chaitanya.recipedata.api.ApiService
import com.chaitanya.recipedata.models.AllProductsResponse
import com.chaitanya.recipedata.models.RandomRecipeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getPopularData(): Response<RandomRecipeResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getRandomRecipes()
        }
    }

    suspend fun getAllData(): Response<AllProductsResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getAllRecipes()
        }
    }
}