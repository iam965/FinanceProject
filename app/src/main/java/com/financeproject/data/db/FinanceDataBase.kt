package com.financeproject.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    version = 2,
    entities = [Operation::class]
)
abstract class FinanceDataBase : RoomDatabase() {
    abstract fun getOperationDao(): OperationDao

    companion object {
        private var Instance: FinanceDataBase? = null

        fun getDatabase(context: Context): FinanceDataBase {
            return Instance ?: synchronized(this) {
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