package com.mindera.flickergallery.models

data class ImageItems(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val photo: MutableList<ImageItem>,
    val total: String
)