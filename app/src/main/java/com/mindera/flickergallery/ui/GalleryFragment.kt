package com.mindera.flickergallery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mindera.flickergallery.MainActivity
import com.mindera.flickergallery.R
import com.mindera.flickergallery.databinding.FragmentGalleryBinding
import com.mindera.flickergallery.utils.Constants
import com.mindera.flickergallery.utils.Resource

class GalleryFragment: Fragment(){

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var viewModel: GalleryViewModel
    private lateinit var galleryAdapter: GalleryAdapter

    // pagination control variables
    private var isLoading = false
    private val isLastPage = false
    private var isScrolling = false

    // listener to handle scroll events on recycler view
    private val rvScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            // set scrolling to true when user is scrolling
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            // obtain current scroll position
            val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
            val firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = gridLayoutManager.childCount
            val totalItemCount = gridLayoutManager.itemCount

            // determine conditions for pagination
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.DEFAULT_PAGE_SIZE
            val shouldPaginate = (isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling)

            // check if a new page should be fetched
            if (shouldPaginate) {
                viewModel.getImageItems()
                isScrolling = false
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireActivity() as MainActivity).galleryViewModel

        setupRecyclerView()

        galleryAdapter.setOnItemClickListener { image ->
            // put the clicked images information the bundle

            // put the clicked images information the bundle
            val bundle = Bundle().apply {
                putSerializable("image", image)
            }

            // navigate to the image full screen fragment
            findNavController().navigate(R.id.action_galleryFragment_to_largeImageFragment,bundle)
        }

        // setup the observer for the image items response
        viewModel.galleryLiveData.observe(viewLifecycleOwner) { response ->
            if (response is Resource.Success<*>) {
                hideProgressBar()
                if (response.data != null) {
                    val responseItems = response.data.photos.photo

                    // update the recycler view
                    galleryAdapter.submitItems(responseItems)
                    galleryAdapter.notifyDataSetChanged()

                    // update isLoading for pagination
                    val totalPages: Int = response.data.photos.total.toInt() / Constants.DEFAULT_PAGE_SIZE + 1
                    isLoading = viewModel.galleryPage == totalPages
                }
            } else if (response is Resource.Error<*>) {
                hideProgressBar()
                if (response.message != null) {
                    Toast.makeText(
                        requireActivity(),
                        "Error: " + response.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else if (response is Resource.Loading<*>) {
                showProgressBar()
            }
        }

        // get initial page of images
        if (viewModel.galleryPage == 1) {
            viewModel.getImageItems()
        }
    }

    /**
     * Sets up the Recycler View that will present the gallery.
     */
    private fun setupRecyclerView() {

        val gridLayoutManager = GridLayoutManager(context, 2)
        galleryAdapter = GalleryAdapter()
        binding.imageRecyclerView.layoutManager = gridLayoutManager
        binding.imageRecyclerView.adapter = galleryAdapter
        binding.imageRecyclerView.addOnScrollListener(rvScrollListener)
    }

    private fun hideProgressBar() {
        binding.loading.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.loading.visibility = View.VISIBLE
        isLoading = true
    }
}