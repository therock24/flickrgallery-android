package com.mindera.flickergallery.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mindera.flickergallery.R
import com.mindera.flickergallery.databinding.FragmentGalleryBinding
import com.mindera.flickergallery.models.ImageItem
import com.mindera.flickergallery.ui.adapter.ImageListAdapter
import com.mindera.flickergallery.utils.Constants
import io.reactivex.disposables.Disposable

class FragmentGallery: Fragment(), GalleryContract.View{

    private lateinit var binding: FragmentGalleryBinding

    lateinit var presenter: GalleryContract.Presenter

    private var itemAdapter: ImageListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        presenter.loadInitialImages(false)
    }

    private fun setupRecyclerView() {

        val gridLayoutManager = GridLayoutManager(context,2)
        binding.imageRecyclerView.layoutManager = gridLayoutManager

        //listen to scrolling, and calculate page number to load new items
        //custom support for pagination, improves performance
        val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = gridLayoutManager.childCount
                val totalItemCount = gridLayoutManager.itemCount
                val firstVisibleItems = IntArray(2)
                val firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems)[0]
                if (!(presenter as GalleryPresenter).isLoading && !(presenter as GalleryPresenter).isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        presenter.loadMoreImages(false)
                    }
                }
            }
        }

        //set the scroll listener
        binding.imageRecyclerView.addOnScrollListener(recyclerViewOnScrollListener)
    }

    override fun initImagesList(imageItems: List<ImageItem>) {
        TODO("Not yet implemented")
    }

    override fun refreshImagesList() {
        TODO("Not yet implemented")
    }

    override fun showLoadingAnimation() {
        TODO("Not yet implemented")
    }

    override fun hideLoadingAnimation() {
        TODO("Not yet implemented")
    }

    override fun showNoDataUI() {
        TODO("Not yet implemented")
    }

}