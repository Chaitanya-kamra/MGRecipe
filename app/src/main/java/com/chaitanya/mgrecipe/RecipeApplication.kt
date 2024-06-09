package com.chaitanya.mgrecipe

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RecipeApplication:Application() {
    companion object {
        lateinit var instance: RecipeApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}