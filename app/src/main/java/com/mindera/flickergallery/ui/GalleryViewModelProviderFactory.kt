package com.mindera.flickergallery.ui

import android.app.Application
import com.mindera.flickergallery.repository.GalleryRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Creates a [GalleryViewModel] with a [GalleryRepository] attached to it.
 */
class GalleryViewModelProviderFactory(
    val app: Application,
    private val galleryRepository: GalleryRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GalleryViewModel(app,galleryRepository) as T
    }
}