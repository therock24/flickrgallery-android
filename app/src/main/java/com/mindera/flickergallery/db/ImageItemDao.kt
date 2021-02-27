package com.mindera.flickergallery.db

import androidx.room.*
import com.mindera.flickergallery.models.ImageItem
import io.reactivex.Flowable

@Dao
interface ImageItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingleImage(imageItem: ImageItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultipleImage(imageItemList: List<ImageItem>)

    @Query("SELECT * FROM photos WHERE id = :itemId")
    fun fetchItemByImageId(itemId: Int): Flowable<ImageItem>

    @Query("SELECT * FROM photos")
    fun fetchImages(): Flowable<List<ImageItem>>

    @Update
    fun updateImage(imageItem: ImageItem)

    @Delete
    fun deleteImage(imageItem: ImageItem)

}