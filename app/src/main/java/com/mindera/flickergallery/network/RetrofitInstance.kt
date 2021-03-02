package com.mindera.flickergallery.network

import com.mindera.flickergallery.utils.Constants
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Builds a Retrofit instance to handle requests to obtain lists of images from the Flickr API.
 */
class RetrofitInstance {
    companion object {

        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build()

            Retrofit.Builder()
                    .baseUrl(Constants.FLICKR_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
        }

        val api by lazy {
            retrofit.create(ApiService::class.java)
        }

    }
}