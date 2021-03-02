package com.mindera.flickergallery.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mindera.flickergallery.models.ImageItem

/**
 * Data access object to obtain images from the Room Database.
 */
@Dao
interface GalleryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: ImageItem)

    @Query("SELECT * FROM images")
    fun getAllImages(): LiveData<List<ImageItem>>

    @Delete
    suspend fun deleteImage(item: ImageItem)
}