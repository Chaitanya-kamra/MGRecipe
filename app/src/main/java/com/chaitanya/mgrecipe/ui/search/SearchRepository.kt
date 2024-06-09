package com.chaitanya.mgrecipe.ui.search

import com.chaitanya.recipedata.api.ApiService
import com.chaitanya.recipedata.models.RandomRecipeResponse
import com.chaitanya.recipedata.models.SearchQueryResponseItem
import com.chaitanya.recipedata.models.SimilarRecipeResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class SearchRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getSearchReponse(query:String): Response<ArrayList<SearchQueryResponseItem>> {
        return withContext(Dispatchers.IO) {
            apiService.getAutoCompleteSearch(query = query)
        }
    }
    suspend fun getSimilarRecipe(id:String): Response<ArrayList<SimilarRecipeResponseItem>> {
        return withContext(Dispatchers.IO) {
            apiService.getSimitarRecipebyId(id)
        }
    }
}