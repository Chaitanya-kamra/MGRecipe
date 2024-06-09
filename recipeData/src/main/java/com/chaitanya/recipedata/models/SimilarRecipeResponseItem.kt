package com.chaitanya.recipedata.models

data class SimilarRecipeResponseItem(
    val id: Long,
    val title: String,
    val imageType: String,
    val readyInMinutes: Long,
    val servings: Long,
    val sourceUrl: String,
)