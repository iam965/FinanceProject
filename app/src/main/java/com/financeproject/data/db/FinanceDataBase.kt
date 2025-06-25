package com.financeproject.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE operations_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                value REAL NOT NULL,
                date TEXT NOT NULL,
                description TEXT NOT NULL,
                is_profit INTEGER NOT NULL,
                categoryId INTEGER NOT NULL DEFAULT 4,
                FOREIGN KEY(categoryId) REFERENCES category(id) ON DELETE CASCADE
            )
        """.trimIndent())
        database.execSQL("""
            INSERT INTO operations_new (id, value, date, description, is_profit, categoryId)
            SELECT id, value, date, description, is_profit, 4 FROM operations
        """.trimIndent())
        database.execSQL("DROP TABLE operations")
        database.execSQL("ALTER TABLE operations_new RENAME TO operations")
    }
}

@Database(
    version = 4,
    entities = [Operation::class, CurrencyEntity::class, Category::class]
)
abstract class FinanceDataBase : RoomDatabase() {
    abstract fun getOperationDao(): OperationDao
    abstract fun getCurrencyDao(): CurrencyDao
    abstract fun getCategoryDao(): CategoryDao

    companion object {
        private var Instance: FinanceDataBase? = null

        fun getDatabase(context: Context): FinanceDataBase {
            return Instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FinanceDataBase::class.java,
                    "finance_db"
                )
                    .addMigrations(MIGRATION_3_4)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            db.execSQL("INSERT INTO category (id, category, is_profit) VALUES (1, 'Зарплата', 1)")
                            db.execSQL("INSERT INTO category (id, category, is_profit) VALUES (2, 'Подарки', 1)")
                            db.execSQL("INSERT INTO category (id, category, is_profit) VALUES (3, 'Бизнес', 1)")
                            db.execSQL("INSERT INTO category (id, category, is_profit) VALUES (4, 'Другое', 1)")
                            db.execSQL("INSERT INTO category (id, category, is_profit) VALUES (5, 'Развлечения', 0)")
                            db.execSQL("INSERT INTO category (id, category, is_profit) VALUES (6, 'Питание', 0)")
                            db.execSQL("INSERT INTO category (id, category, is_profit) VALUES (7, 'Коммунальные услуги', 0)")
                            db.execSQL("INSERT INTO category (id, category, is_profit) VALUES (8, 'Образование', 0)")
                            db.execSQL("INSERT INTO category (id, category, is_profit) VALUES (9, 'Транспорт', 0)")
                            db.execSQL("INSERT INTO category (id, category, is_profit) VALUES (10, 'Пожертвования', 0)")
                            db.execSQL("INSERT INTO category (id, category, is_profit) VALUES (11, 'Штрафы', 0)")
                            db.execSQL("INSERT INTO category (id, category, is_profit) VALUES (12, 'Другое', 0)")
                        }
                    })
                    .build()
                Instance = instance
                return instance
            }
        }
    }
}