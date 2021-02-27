package com.mindera.flickergallery.ui

import com.mindera.flickergallery.models.ImageItem
import com.mindera.flickergallery.utils.Constants
import io.reactivex.disposables.Disposable

class GalleryPresenter: GalleryContract.Presenter {

    protected var view: V? = null

    private var disposable: Disposable? = null

    //indicates whether it last page
    internal var isLastPage = false

    //indicates whether the items is loading elements or not
    internal var isLoading = false

    //holds the list of all the photos loaded
    internal var photoList: MutableList<ImageItem> = mutableListOf()

    //holds the current page
    internal var page: Int = 1

    //holds the num element per page
    private val perPage: Int = Constants.DEFAULT_PAGE_SIZE


    /**
     * Load the photos list from the data source and update the display for the first time
     * Uses Pagination indicated by [page] and limit [perPage]
     */
    override fun loadInitialImages(refresh: Boolean) {

        isLoading = true

        view?.showProgressDialog()

        if (refresh)
            dataSource.refreshItems()

        //remove the previous disposable from composite disposable, for multiple load items calls
        if (disposable != null)
            compositeDisposable.delete(disposable!!)

        disposable = dataSource.getPhotoItemList(key!!, query, page, perPage)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ photoItems: List<PhotoItem> ->
                    if (!isViewAttached)
                        return@subscribe

                    isLoading = false

                    view?.dismissProgressDialog()

                    if (photoItems.isNotEmpty()) {
                        photoList.clear()
                        photoList.addAll(photoItems)
                        view?.initItemList(photoList)
                    }else
                        view?.showEmptyListUI()
                }, { throwable: Throwable? ->
                    if (!isViewAttached)
                        return@subscribe

                    isLoading = false

                    view?.dismissProgressDialog()
                    handleApiError(throwable)
                })

        compositeDisposable.add(disposable!!)
    }

    /**
     * Load the photos list from the data source and update the display
     * Uses Pagination indicated by [page] and limit [perPage]
     */
    override fun loadMoreImages(refresh: Boolean) {

        //key is required for the call
        if (key.isNullOrEmpty()) {
            view?.showSnackBarMessage(R.string.default_error_message)
            return
        }

        //end point reached no need to get more data
        if (dataSource.getPaginationStatus()) {
            isLastPage = true
            return
        }

        this.query = query

        //increment the page number
        page++

        isLoading = true

        view?.showBottomLoading()

        if (refresh)
            dataSource.refreshItems()

        //remove the previous disposable from composite disposable, for multiple load items calls
        if (disposable != null)
            compositeDisposable.delete(disposable!!)

        disposable = dataSource.getPhotoItemList(key!!, query, page, perPage)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ photoItems: List<PhotoItem> ->
                    if (!isViewAttached)
                        return@subscribe

                    isLoading = false

                    view?.hideBottomLoading()

                    if (photoItems.isNotEmpty()) {
                        photoList.addAll(photoItems)
                        view?.refreshItemList()
                    }

                }, { throwable: Throwable? ->
                    if (!isViewAttached)
                        return@subscribe

                    isLoading = false

                    view?.hideBottomLoading()
                    handleApiError(throwable)
                })

        compositeDisposable.add(disposable!!)
    }

}