package com.financeproject.ui.state

import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme

data class UIState(
    val sharedPrefs: SharedPreferences,
    var isDarkTheme: Boolean = sharedPrefs.getBoolean("is_Dark_Theme", false),
    var currentScreen: String = "Home"
) {

    fun copy(IsDarkTheme: Boolean = this.isDarkTheme, CurrentScreen: String = this.currentScreen): UIState{
        return UIState(sharedPrefs, IsDarkTheme, CurrentScreen)
    }

    fun changeTheme(){
        with(sharedPrefs.edit()){
            putBoolean("is_Dark_Theme", !isDarkTheme)
            apply()
        }
    }
}