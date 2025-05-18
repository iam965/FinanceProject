package com.financeproject.ui.state

import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.key

data class UIState(
    val sharedPrefs: SharedPreferences,
    var isDarkTheme: Boolean = sharedPrefs.getBoolean("is_Dark_Theme", false),
    var selectedValute: String? = sharedPrefs.getString("valute", "₽")
) {

    fun copy(): UIState{
        return UIState(sharedPrefs, isDarkTheme, selectedValute)
    }

    fun changeTheme(){
        isDarkTheme = !isDarkTheme
        with(sharedPrefs.edit()){
            putBoolean("is_Dark_Theme", isDarkTheme)
            apply()
        }
    }

    fun changeValute(valute: String){
        selectedValute = valute
        with(sharedPrefs.edit()){
            putString("valute", valute)
            apply()
        }
    }
}