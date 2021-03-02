package com.mindera.flickergallery.network

import com.mindera.flickergallery.models.ImageItemsResponse
import com.mindera.flickergallery.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * This service contains the Flickr API requests regarding images.
 */
interface ApiService {

    @GET("rest/?method=" + Constants.METHOD_SEARCH + "&api_key=" + Constants.FLICKR_API_KEY +
            "&tags="+ Constants.DEFAULT_QUERY + "&format=json" + "&nojsoncallback=1" + "&extras=url_q")
    suspend fun getImageList(@Query("page") page: Int,
                     @Query("per_page") perPage: Int): Response<ImageItemsResponse>

}

