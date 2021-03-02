package com.mindera.flickergallery.utils

/**
 * Utils for generating each image URL from the image list fetched from Flickr API.
 */
object URLUtils {

    // image size values
    const val LARGE_SQUARE_SIZE = "q"
    const val LARGE_SIZE = "b"

    fun getFlickrImageLink(id: String, secret: String, serverId: String, farmId: Int, size: String): String {
        return "https://farm$farmId.staticflickr.com/$serverId/${id}_${secret}_$size.jpg"
    }
}