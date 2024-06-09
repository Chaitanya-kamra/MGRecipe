package com.chaitanya.recipedata.utility

object ApiConstants {
    const val BASE_URL = "https://api.spoonacular.com/"
    const val GET_RANDOM_RECIPES = "recipes/random"
    const val GET_ALL_RECIPES = "recipes/complexSearch"
    const val GET_AUTOCOMPLETE_SEARCH = "recipes/autocomplete"
    const val GET_RECIPE_BY_ID = "recipes/{id}/information"
    const val GET_RECIPE_EQUIPMENTS = "recipes/{id}/equipmentWidget.json"
    const val GET_SIMILAR_RECIPES = "recipes/{id}/similar"

}