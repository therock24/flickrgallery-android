package com.mindera.flickergallery

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import com.google.common.truth.Truth.assertThat
import com.mindera.flickergallery.db.GalleryDatabase
import com.mindera.flickergallery.models.ImageItem
import com.mindera.flickergallery.utils.MainCoroutineRule
import com.mindera.flickergallery.utils.getOrAwaitValueTest
import com.mindera.flickergallery.repository.GalleryRepository
import com.mindera.flickergallery.ui.GalleryViewModel
import com.mindera.flickergallery.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * Set of tests for the Gallery View Model.
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
@ExperimentalCoroutinesApi
class GalleryViewModelTest {

    private lateinit var viewModel: GalleryViewModel
    private lateinit var galleryRespository: GalleryRepository
    private lateinit var database: GalleryDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        val application = RuntimeEnvironment.application
        database = Room.inMemoryDatabaseBuilder(application,GalleryDatabase::class.java).allowMainThreadQueries().build()
        galleryRespository = GalleryRepository(database)
        viewModel = GalleryViewModel(application,galleryRespository)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `insert image item with empty id, returns error`() {
        val image = ImageItem("",0,0,0,0,"","","","","123",0,0)

        viewModel.saveImage(image)

        val value = viewModel.galleryRespositoryLiveData.getOrAwaitValueTest()


        assertThat(value).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `insert image item with empty url, returns error`() {
        val image = ImageItem("123",0,0,0,0,"","","","","",0,0)

        viewModel.saveImage(image)

        val value = viewModel.galleryRespositoryLiveData.getOrAwaitValueTest()

        assertThat(value).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `insert image item with valid input, returns success`() {
        val image = ImageItem("1",0,0,0,0,"","","","","http://google.com",0,0)

        viewModel.saveImage(image)

        val value = viewModel.galleryRespositoryLiveData.getOrAwaitValueTest()

        assertThat(value).isInstanceOf(Resource.Success::class.java)
    }

    @Test
    fun `delete image item with empty id, returns error`() {
        val image = ImageItem("",0,0,0,0,"","","","","http://google.com",0,0)

        viewModel.deleteImage(image)

        val value = viewModel.galleryRespositoryLiveData.getOrAwaitValueTest()

        assertThat(value).isInstanceOf(Resource.Error::class.java)
    }
}