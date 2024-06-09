package com.chaitanya.recipedata.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class SingleRecipeResponse(
    val vegetarian: Boolean,
    val vegan: Boolean,
    val glutenFree: Boolean,
    val dairyFree: Boolean,
    val veryHealthy: Boolean,
    val cheap: Boolean,
    val veryPopular: Boolean,
    val sustainable: Boolean,
    val lowFodmap: Boolean,
    val weightWatcherSmartPoints: Long,
    val gaps: String,
    val preparationMinutes: Any?,
    val cookingMinutes: Any?,
    val aggregateLikes: Long,
    val healthScore: Long,
    val creditsText: String,
    val license: String,
    val sourceName: String,
    val pricePerServing: Double,
    val extendedIngredients: List<ExtendedIngredient>,
    val id: Long,
    val title: String,
    val readyInMinutes: Long,
    val servings: Long,
    val sourceUrl: String,
    val image: String,
    val imageType: String,
    val nutrition: Nutrition,
    val summary: String,
    val cuisines: List<Any?>,
    val dishTypes: List<String>,
    val diets: List<Any?>,
    val occasions: List<Any?>,
    val instructions: String,
    val analyzedInstructions: List<Any?>,
    val originalId: Any?,
    val spoonacularScore: Double,
    val spoonacularSourceUrl: String,
)
@Parcelize
data class Nutrition(
    val nutrients: List<Nutrient>,
    val properties: List<Property>,
    val flavonoids: List<Flavonoid>,
    val ingredients: List<IngredientWithNutrient>,
    val caloricBreakdown: CaloricBreakdown,
    val weightPerServing: WeightPerServing,
) : Parcelable

@Parcelize
data class Nutrient(
    val name: String,
    val amount: Double,
    val unit: String,
    val percentOfDailyNeeds: Double,
) : Parcelable

@Parcelize
data class Property(
    val name: String,
    val amount: Double,
    val unit: String,
) : Parcelable

@Parcelize
data class Flavonoid(
    val name: String,
    val amount: Double,
    val unit: String,
) : Parcelable

@Parcelize
data class IngredientWithNutrient(
    val id: Long,
    val name: String,
    val amount: Double,
    val unit: String,
    val nutrients: List<Nutrient2>,
) : Parcelable

@Parcelize
data class Nutrient2(
    val name: String,
    val amount: Double,
    val unit: String,
    val percentOfDailyNeeds: Double,
) : Parcelable

@Parcelize
data class CaloricBreakdown(
    val percentProtein: Double,
    val percentFat: Double,
    val percentCarbs: Double,
) : Parcelable

@Parcelize
data class WeightPerServing(
    val amount: Long,
    val unit: String,
) : Parcelable

fun formatNutrition(nutrition: Nutrition): String {
    val builder = StringBuilder()

    builder.append("Nutrients:\n")
    nutrition.nutrients.forEach {
        builder.append("  - ${it.name}: ${it.amount}${it.unit} (${it.percentOfDailyNeeds}% of daily needs)\n")
    }

    builder.append("\nCaloric Breakdown:\n")
    builder.append("  - Protein: ${nutrition.caloricBreakdown.percentProtein}%\n")
    builder.append("  - Fat: ${nutrition.caloricBreakdown.percentFat}%\n")
    builder.append("  - Carbs: ${nutrition.caloricBreakdown.percentCarbs}%\n")

    builder.append("\nWeight Per Serving:\n")
    builder.append("  - ${nutrition.weightPerServing.amount}${nutrition.weightPerServing.unit}\n")

    return builder.toString()
}
