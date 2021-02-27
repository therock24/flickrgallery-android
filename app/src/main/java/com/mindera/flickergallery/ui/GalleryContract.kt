package com.mindera.flickergallery.ui

import com.mindera.flickergallery.models.ImageItem

interface GalleryContract {

    interface View {

        fun initImagesList(imageItems: List<ImageItem>)

        fun refreshImagesList()

        fun showLoadingAnimation()

        fun hideLoadingAnimation()

        fun showNoDataUI()

    }

    interface Presenter {

        fun loadInitialImages(refresh: Boolean)

        fun loadMoreImages(refresh: Boolean)

    }
}