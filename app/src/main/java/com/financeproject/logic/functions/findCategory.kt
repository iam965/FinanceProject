package com.financeproject.logic.functions

import com.financeproject.data.db.Category

fun findCategory(id: Int, category: List<Category>): Category? {
    for (cat in category) {
        if (cat.id == id){
            return cat
        }
    }
    return null
}