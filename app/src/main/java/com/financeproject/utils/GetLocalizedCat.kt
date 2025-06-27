package com.financeproject.utils

import android.content.Context
import com.financeproject.R
import com.financeproject.data.db.Category

fun getLocalizedCat(context: Context, category: Category): String {
    val catId = when (category.id) {
        1 -> R.string.cat_salary
        2 -> R.string.cat_gift
        3 -> R.string.cat_business
        4 -> R.string.cat_other_i
        5 -> R.string.cat_entertainment
        6 -> R.string.cat_nutrition
        7 -> R.string.cat_utilities
        8 -> R.string.cat_education
        9 -> R.string.cat_transport
        10 -> R.string.cat_donation
        11 -> R.string.cat_fine
        12 -> R.string.cat_other_e
        else -> null
    }
    return if (catId != null){
        context.getString(catId)
    }
    else{
        category.category
    }
}