package com.mindera.flickergallery.repository

import com.mindera.flickergallery.models.ImageItem
import io.reactivex.Flowable

interface ImageRepository {

    fun getPhotoItemList(page: Int, perPage: Int): Flowable<List<ImageItem>>

    fun getCachedPhotoItems(): Flowable<List<ImageItem>>

    fun getPaginationStatus(): Boolean

    fun getPageNumber(): Int

    fun getMaxPageNumber(): Int

    fun refreshItems()
}