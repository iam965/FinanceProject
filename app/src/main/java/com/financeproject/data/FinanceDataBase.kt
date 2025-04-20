package com.financeproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1,
    entities = [Category::class,
    Operation::class])
abstract class FinanceDataBase : RoomDatabase(){
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getOperationDao(): OperationDao

    companion object{
        private var Instance: FinanceDataBase? = null

        fun getDatabase(context: Context):FinanceDataBase {
            return Instance?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FinanceDataBase::class.java,
                    "notes_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                Instance = instance
                return instance
            }
        }
    }
}