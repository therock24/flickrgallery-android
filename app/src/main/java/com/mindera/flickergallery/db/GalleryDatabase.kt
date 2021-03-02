package com.mindera.flickergallery.db

import com.mindera.flickergallery.models.ImageItem

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/**
 * Room database to store images fetched from the Flickr API.
 */
@Database(
    entities = [ImageItem::class],
    version = 2,
    exportSchema = false
)

abstract class GalleryDatabase: RoomDatabase() {

    abstract fun getGalleryDao(): GalleryDao

    companion object {
        @Volatile
        private var instance: GalleryDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                GalleryDatabase::class.java,
                "gallery_db.db").build()
    }
}