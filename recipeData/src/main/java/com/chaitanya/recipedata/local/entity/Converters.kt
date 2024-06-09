package com.chaitanya.recipedata.local.entity

import androidx.room.TypeConverter
import com.chaitanya.recipedata.models.EquipmentSingleRecipe
import com.chaitanya.recipedata.models.ExtendedIngredient
import com.chaitanya.recipedata.models.Nutrition
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromNutrition(nutrition: Nutrition): String {
        return Gson().toJson(nutrition)
    }

    @TypeConverter
    fun toNutrition(nutritionString: String): Nutrition {
        return Gson().fromJson(nutritionString, Nutrition::class.java)
    }

    @TypeConverter
    fun fromExtendedIngredients(extendedIngredients: List<ExtendedIngredient>): String {
        return Gson().toJson(extendedIngredients)
    }

    @TypeConverter
    fun toExtendedIngredients(extendedIngredientsString: String): List<ExtendedIngredient> {
        val listType = object : TypeToken<List<ExtendedIngredient>>() {}.type
        return Gson().fromJson(extendedIngredientsString, listType)
    }

    @TypeConverter
    fun fromEquipment(equipment: List<EquipmentSingleRecipe>): String {
        return Gson().toJson(equipment)
    }

    @TypeConverter
    fun toEquipment(equipmentString: String): List<EquipmentSingleRecipe> {
        val listType = object : TypeToken<List<EquipmentSingleRecipe>>() {}.type
        return Gson().fromJson(equipmentString, listType)
    }
}
