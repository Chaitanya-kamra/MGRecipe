package com.chaitanya.mgrecipe.ui.detail

import androidx.lifecycle.LiveData
import com.chaitanya.recipedata.api.ApiService
import com.chaitanya.recipedata.local.RecipeDatabase
import com.chaitanya.recipedata.local.entity.RecipeEntity
import com.chaitanya.recipedata.models.AllProductsResponse
import com.chaitanya.recipedata.models.RandomRecipeResponse
import com.chaitanya.recipedata.models.RecipeEquipmentResponse
import com.chaitanya.recipedata.models.SingleRecipeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class DetailRepository @Inject constructor(private val apiService: ApiService,private val database: RecipeDatabase) {
    suspend fun getRecipeById(id:String): Response<SingleRecipeResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getRecipebyId(id = id)
        }
    }
    suspend fun getRecipeEquipmentById(id:String): Response<RecipeEquipmentResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getRecipeEquipmentsbyId(id = id)
        }
    }
    suspend fun addToFourite(recipe: RecipeEntity) {
        withContext(Dispatchers.IO){
            database.noteDao().insert(recipe)
        }
    }
    suspend fun removeFromFourite(id:Long) {
        withContext(Dispatchers.IO){
            database.noteDao().deleteById(id)
        }
    }
    suspend fun isRecipeExist(id:Long): Boolean {
        return withContext(Dispatchers.IO){
            database.noteDao().isRecipeExists(id)>0
        }
    }
    fun getFavouriteRecipes(): LiveData<List<RecipeEntity>> {
         return database.noteDao().getAllRecipes()
    }
    suspend fun getFavouriteRecipeById(id:Long): RecipeEntity?{
        return withContext(Dispatchers.IO){ database.noteDao().getRecipeById(id)}
    }


}