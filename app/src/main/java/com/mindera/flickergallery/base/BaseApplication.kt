package com.mindera.flickergallery.base

import android.app.Application
import com.mindera.flickergallery.repository.ImageDataRepository

class BaseApplication: Application() {

    lateinit var mRepository: ImageDataRepository

    override fun onCreate() {
        super.onCreate()


    }


}