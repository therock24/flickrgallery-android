package com.mindera.flickergallery.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mindera.flickergallery.GalleryApplication
import com.mindera.flickergallery.models.ImageItem
import com.mindera.flickergallery.models.ImageItemsResponse
import com.mindera.flickergallery.repository.GalleryRepository
import com.mindera.flickergallery.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

/**
 * ViewModel for [GalleryFragment].
 * Responsible for the business logic, mainly with requesting the image list from Flickr
 * and passing the data to the [GalleryFragment].
 */
class GalleryViewModel(
    app: Application,
    private val galleryRepository: GalleryRepository): AndroidViewModel(app) {

    val galleryLiveData: MutableLiveData<Resource<ImageItemsResponse>> = MutableLiveData()
    val galleryRespositoryLiveData: MutableLiveData<Resource<ImageItem>> = MutableLiveData()
    var galleryPage = 1
    var galleryResponse: ImageItemsResponse? = null

    /**
     * This method issues a request to the Flickr API to obtain the images for the given
     * tag, including the current page (for pagination support).
     */
    fun getImageItems() = viewModelScope.launch {
        safeImageItemsCall()
    }

    /**
     * This method performs a request to Flickr API if and only if there is internet connection.
     */
    private suspend fun safeImageItemsCall() {

        // issue loading state to fragment
        galleryLiveData.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                // request images and post result
                val response = galleryRepository.getImageList(galleryPage)
                galleryLiveData.postValue(handleImagesResponse(response))
            } else {
                galleryLiveData.postValue(Resource.Error("No Internet Connection!"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> galleryLiveData.postValue(Resource.Error("Network Failure!"))
                else -> galleryLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }

    }

    /**
     * Handle the response from the Flickr Api and add new items to the list.
     */
    private fun handleImagesResponse(response: Response<ImageItemsResponse>?): Resource<ImageItemsResponse> {

        response?.let {
            if(response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    // increment page number
                    galleryPage++

                    // check if it is the first response
                    if(galleryResponse == null) {
                        galleryResponse = resultResponse
                    } else {
                        val oldImages = galleryResponse?.photos?.photo
                        val newImages = resultResponse.photos.photo
                        oldImages?.addAll(newImages)
                    }
                    return Resource.Success(galleryResponse ?: resultResponse)
                }
            }
        }

        return Resource.Error(response?.message())
    }

    /**
     * Check if internet connection is available, to avoid making API requests while offline.
     * @return true if internet is available; false otherwise
     */
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<GalleryApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> return true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    fun saveImage(imageItem: ImageItem) = viewModelScope.launch {

        if(imageItem.id.isEmpty() || imageItem.url_q.isEmpty()) {
            galleryRespositoryLiveData.postValue(Resource.Error("The id or url of the image cannot be empty!"))
            return@launch
        }
        galleryRepository.upsert(imageItem)
        galleryRespositoryLiveData.postValue(Resource.Success(imageItem))
    }

    fun getSavedImages() = galleryRepository.getAllImages()

    fun deleteImage(imageItem: ImageItem) = viewModelScope.launch {
        if(imageItem.id.isEmpty()) {
            galleryRespositoryLiveData.postValue(Resource.Error("The id of the image cannot be empty!"))
        }
        galleryRepository.deleteImage(imageItem)
    }
}