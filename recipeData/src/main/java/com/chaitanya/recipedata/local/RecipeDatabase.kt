package com.chaitanya.recipedata.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chaitanya.recipedata.local.entity.Converters
import com.chaitanya.recipedata.local.entity.RecipeEntity

@Database(entities = [RecipeEntity::class],version = 1)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun noteDao(): RecipeDao
}