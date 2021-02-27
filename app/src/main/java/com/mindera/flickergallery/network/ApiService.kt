package com.mindera.flickergallery.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mindera.flickergallery.models.ImageItem
import com.mindera.flickergallery.utils.Constants
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.FLICKR_URL)
    .build()

interface ApiService {

    @GET("rest/?method=" + Constants.METHOD_SEARCH + "&api_key=" + Constants.FLICKR_API_KEY +
            "&tags="+ Constants.DEFAULT_QUERY + "&format=json" + "&nojsoncallback=1" + "&extras=url_q")
    fun getImageList(@Query("page") page: Int,
                     @Query("per_page") perPage: Int): Flowable<Response<ImageItem>>

}

object API {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}