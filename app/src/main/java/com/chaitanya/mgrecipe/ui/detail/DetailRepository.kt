package com.chaitanya.mgrecipe.ui.detail

import androidx.lifecycle.LiveData
import com.chaitanya.recipedata.api.ApiService
import com.chaitanya.recipedata.local.RecipeDatabase
import com.chaitanya.recipedata.local.entity.RecipeEntity
import com.chaitanya.recipedata.models.RecipeEquipmentResponse
import com.chaitanya.recipedata.models.SingleRecipeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject


//Repository class to handle data operations related to recipe details.
class DetailRepository @Inject constructor(
    private val apiService: ApiService,
    private val database: RecipeDatabase
) {

    //Fetches a recipe by its ID from the API.
    suspend fun getRecipeById(id: String): Response<SingleRecipeResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getRecipebyId(id = id)
        }
    }

    //Adds a recipe to the favorites in the local database
    suspend fun getRecipeEquipmentById(id: String): Response<RecipeEquipmentResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getRecipeEquipmentsbyId(id = id)
        }
    }
    //Removes a recipe from the favorites in the local database by its ID.
    suspend fun addToFourite(recipe: RecipeEntity) {
        withContext(Dispatchers.IO) {
            database.noteDao().insert(recipe)
        }
    }

    //Checks if a recipe exists in the favorites in the local database by its ID
    suspend fun removeFromFourite(id: Long) {
        withContext(Dispatchers.IO) {
            database.noteDao().deleteById(id)
        }
    }

    //Retrieves all favorite recipes from the local database.
    suspend fun isRecipeExist(id: Long): Boolean {
        return withContext(Dispatchers.IO) {
            database.noteDao().isRecipeExists(id) > 0
        }
    }

    //Retrieves a favorite recipe from the local database by its ID.
    fun getFavouriteRecipes(): LiveData<List<RecipeEntity>> {
        return database.noteDao().getAllRecipes()
    }

    /**
     * Retrieves a favorite recipe from the local database by its ID.
     * @param id The ID of the recipe to retrieve.
     * @return The RecipeEntity if found, null otherwise.
     */
    suspend fun getFavouriteRecipeById(id: Long): RecipeEntity? {
        return withContext(Dispatchers.IO) { database.noteDao().getRecipeById(id) }
    }


}