package com.mindera.flickergallery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mindera.flickergallery.MainActivity
import com.mindera.flickergallery.R
import com.mindera.flickergallery.databinding.FragmentGalleryBinding
import com.mindera.flickergallery.databinding.FragmentLargeImageBinding
import com.mindera.flickergallery.utils.URLUtils

/**
 * Fragment that shows a single image in the Large Size.
 * For this, the Large size image needs to be fetched from Flickr.
 */
class LargeImageFragment: Fragment() {

    private lateinit var binding: FragmentLargeImageBinding
    val args: LargeImageFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_large_image, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get image URL
        val image = args.image
        val imageUrl = URLUtils.getFlickrImageLink(
            image.id,
            image.secret,
            image.server,
            image.farm,
            URLUtils.LARGE_SIZE
        )

        // fetch large size image
        Glide.with(view).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.DATA).into(binding.ivLargeImage)
    }
}