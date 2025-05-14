package com.financeproject.ui.state

import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme

data class UIState(
    val sharedPrefs: SharedPreferences,
    var isDarkTheme: Boolean = sharedPrefs.getBoolean("is_Dark_Theme", false)
) {

    fun copy(): UIState{
        return UIState(sharedPrefs, isDarkTheme)
    }

    fun changeTheme(){
        isDarkTheme = !isDarkTheme
        with(sharedPrefs.edit()){
            putBoolean("is_Dark_Theme", isDarkTheme)
            apply()
        }
    }
}