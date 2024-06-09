package com.chaitanya.recipedata.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chaitanya.recipedata.local.entity.RecipeEntity


@Dao
interface RecipeDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: RecipeEntity)

    @Query("SELECT * FROM recipe_table WHERE id = :id")
    suspend fun getRecipeById(id: Long): RecipeEntity?

    @Delete
    suspend fun delete(note: RecipeEntity)

    @Query("DELETE FROM recipe_table WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM recipe_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM recipe_table")
    fun getAllRecipes(): LiveData<List<RecipeEntity>>

    @Query("SELECT COUNT(*) FROM recipe_table WHERE id = :id")
    suspend fun isRecipeExists(id: Long): Int
}