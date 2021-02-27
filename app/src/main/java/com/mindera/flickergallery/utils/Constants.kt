package com.mindera.flickergallery.utils

object Constants {

    const val FLICKR_URL = "https://api.flickr.com/services/"
    const val FLICKR_API_KEY = "9a95c68a9c6ec61104cd3967dcbb8bd3"

    const val DEFAULT_PAGE_SIZE = 25
    const val DEFAULT_QUERY = "kitten"

    const val METHOD_GET_SIZES = "flickr.photos.getSizes"
    const val METHOD_SEARCH = "flickr.photos.search"

    const val LARGE_SQUARE = "Large Square"

    fun getFlickrImageLink(id: String, secret: String, serverId: String, farmId: Int, size: String): String {
        return "https://farm$farmId.staticflickr.com/$serverId/${id}_${secret}_$size.jpg"
    }

}