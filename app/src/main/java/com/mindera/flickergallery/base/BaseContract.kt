package com.mindera.flickergallery.base

interface BaseContract {

    interface View<T> {

        fun showProgressDialog()

        fun dismissProgressDialog()

    }

    interface Presenter<V> {

        fun onAttach(view: V)

        fun onDetach()
    }
}