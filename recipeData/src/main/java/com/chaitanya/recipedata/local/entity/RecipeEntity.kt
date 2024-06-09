package com.chaitanya.recipedata.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chaitanya.recipedata.models.EquipmentSingleRecipe
import com.chaitanya.recipedata.models.ExtendedIngredient
import com.chaitanya.recipedata.models.Nutrition
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "recipe_table")

data class RecipeEntity(
    @PrimaryKey val id: Long,
    val title: String?,
    val readyInMinutes: Long?,
    val servings: Long?,
    val sourceUrl: String?,
    val image: String?,
    val imageType: String?,
    val nutrition: Nutrition?,
    val summary: String?,
    val instructions: String?,
    val pricePerServing: Double?,
    val extendedIngredients: List<ExtendedIngredient>?,
    val equipment: List<EquipmentSingleRecipe>?

): Parcelable