package com.mindera.flickergallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mindera.flickergallery.db.GalleryDatabase
import com.mindera.flickergallery.repository.GalleryRepository
import com.mindera.flickergallery.ui.GalleryViewModel
import com.mindera.flickergallery.ui.GalleryViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    lateinit var galleryViewModel: GalleryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize viewmodel
        val galleryRepository = GalleryRepository(GalleryDatabase.invoke(this))
        val vmFactory = GalleryViewModelProviderFactory(application,galleryRepository)
        galleryViewModel = ViewModelProvider(this, vmFactory).get(GalleryViewModel::class.java)

        setContentView(R.layout.activity_main)

        // set top toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = getText(R.string.app_name)


    }
}
