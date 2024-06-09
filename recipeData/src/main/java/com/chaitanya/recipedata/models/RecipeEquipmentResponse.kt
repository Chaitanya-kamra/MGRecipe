package com.chaitanya.recipedata.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class RecipeEquipmentResponse(
    val equipment: List<EquipmentSingleRecipe>,
)

@Parcelize
data class EquipmentSingleRecipe(
    val name: String,
    val image: String,
) : Parcelable
