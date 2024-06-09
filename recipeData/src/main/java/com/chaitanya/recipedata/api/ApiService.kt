package com.chaitanya.recipedata.api

import com.chaitanya.recipedata.models.AllProductsResponse
import com.chaitanya.recipedata.models.RandomRecipeResponse
import com.chaitanya.recipedata.models.Recipe
import com.chaitanya.recipedata.models.RecipeEquipmentResponse
import com.chaitanya.recipedata.models.SearchQueryResponseItem
import com.chaitanya.recipedata.models.SimilarRecipeResponseItem
import com.chaitanya.recipedata.models.SingleRecipeResponse
import com.chaitanya.recipedata.utility.ApiConstants.GET_ALL_RECIPES
import com.chaitanya.recipedata.utility.ApiConstants.GET_AUTOCOMPLETE_SEARCH
import com.chaitanya.recipedata.utility.ApiConstants.GET_RANDOM_RECIPES
import com.chaitanya.recipedata.utility.ApiConstants.GET_RECIPE_BY_ID
import com.chaitanya.recipedata.utility.ApiConstants.GET_RECIPE_EQUIPMENTS
import com.chaitanya.recipedata.utility.ApiConstants.GET_SIMILAR_RECIPES
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @GET(GET_RANDOM_RECIPES)
    suspend fun getRandomRecipes(
        @Query("number") number: Int= 10
    ):Response<RandomRecipeResponse>

    @GET(GET_ALL_RECIPES)
    suspend fun getAllRecipes(
        @Query("addRecipeInformation") addRecipeInformation : Boolean = true
    ):Response<AllProductsResponse>

    @GET(GET_AUTOCOMPLETE_SEARCH)
    suspend fun getAutoCompleteSearch(
        @Query("query") query: String,
        @Query("number") number: Int = 8
    ):Response<ArrayList<SearchQueryResponseItem>>

    @GET(GET_RECIPE_BY_ID)
    suspend fun getRecipebyId(
        @Path("id") id: String,
        @Query("includeNutrition") includeNutrition : Boolean = true
    ):Response<SingleRecipeResponse>
    @GET(GET_RECIPE_EQUIPMENTS)
    suspend fun getRecipeEquipmentsbyId(
        @Path("id") id: String
    ):Response<RecipeEquipmentResponse>

    @GET(GET_SIMILAR_RECIPES)
    suspend fun getSimitarRecipebyId(
        @Path("id") id: String,
        @Query("number") number: Int = 10
    ):Response<ArrayList<SimilarRecipeResponseItem>>

}
