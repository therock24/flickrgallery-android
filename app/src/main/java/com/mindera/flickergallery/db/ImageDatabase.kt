package com.mindera.flickergallery.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mindera.flickergallery.models.ImageItem

@Database(entities = [ImageItem::class], version = 1)
abstract class ImageDatabase: RoomDatabase() {

    abstract fun imageItemDao(): ImageItemDao

    companion object {
        @Volatile
        private var INSTANCE: ImageDatabase? = null

        fun getDatabase(context: Context) : ImageDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(context.applicationContext, ImageDatabase::class.java, "notification_data_base").fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}