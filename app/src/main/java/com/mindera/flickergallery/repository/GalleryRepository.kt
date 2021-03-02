package com.mindera.flickergallery.repository

import com.mindera.flickergallery.db.GalleryDatabase
import com.mindera.flickergallery.models.ImageItem
import com.mindera.flickergallery.models.ImageItemsResponse
import com.mindera.flickergallery.network.RetrofitInstance
import com.mindera.flickergallery.utils.Constants
import retrofit2.Response

class GalleryRepository(val db: GalleryDatabase) {

    /**
     * Request a list of images from the Flickr API, including pagination with the page number.
     *
     * @param pageNumber the page to retrieve
     *
     * @return a set of images
     */
    suspend fun getImageList(pageNumber: Int):
            Response<ImageItemsResponse> =
        RetrofitInstance.api.getImageList(pageNumber,Constants.DEFAULT_PAGE_SIZE)

    /**
     * Inserts or updates an [ImageItem] object in the Image Database.
     * @param imageItem the item to insert or update
     */
    suspend fun upsert(imageItem: ImageItem) = db.getGalleryDao().upsert(imageItem)

    /**
     * Gets all the [ImageItem] objects stored in the db.
     * @return a list of images
     */
    fun getAllImages() = db.getGalleryDao().getAllImages()

    /**
     * Deletes a given [ImageItem] object from the db.
     * @param imageItem the item to delete
     */
    suspend fun deleteImage(imageItem: ImageItem) = db.getGalleryDao().deleteImage(imageItem)
}