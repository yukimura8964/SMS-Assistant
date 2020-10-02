package moe.gkd.smsassistant.sql

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import moe.gkd.smsassistant.IApplication
import moe.gkd.smsassistant.entity.ForwardLogEntity
import moe.gkd.smsassistant.sql.dao.ForwardLogDao

@Database(entities = [ForwardLogEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun forwardLogDao(): ForwardLogDao

    companion object {
        private lateinit var instance: AppDatabase
        fun getInstance(): AppDatabase {
            return getInstance(IApplication.instance)
        }

        private fun getInstance(context: Context): AppDatabase {
            if (!this::instance.isInitialized) {
                synchronized(AppDatabase::class.java) {
                    if (!this::instance.isInitialized) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, "app_database"
                        ).build()
                    }
                }
            }
            return instance
        }
    }
}