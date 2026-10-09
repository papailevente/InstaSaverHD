package com.example.instasaverhd

import com.example.instasaverhd.ui.main.FetchState
import com.example.instasaverhd.ui.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeDownloadRepository
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeDownloadRepository()
        viewModel = MainViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onUrlChanged_validUrl_updatesState() {
        viewModel.onUrlChanged("https://www.instagram.com/reel/C123456/")

        val state = viewModel.uiState.value
        assertEquals("https://www.instagram.com/reel/C123456/", state.urlInput)
        assertTrue(state.isValidUrl)
        assertTrue(state.isInstagramUrl)
    }

    @Test
    fun onClearInput_resetsInputState() {
        viewModel.onUrlChanged("https://www.instagram.com/reel/C123456/")
        viewModel.onClearInput()

        val state = viewModel.uiState.value
        assertEquals("", state.urlInput)
        assertFalse(state.isValidUrl)
        assertFalse(state.isInstagramUrl)
        assertEquals(FetchState.Idle, state.fetchState)
        assertNull(state.activePreviewMetadata)
    }

    @Test
    fun fetchMedia_invalidUrl_setsErrorState() = runTest {
        viewModel.onUrlChanged("invalid_url")
        viewModel.fetchMedia()

        val state = viewModel.uiState.value
        assertTrue(state.fetchState is FetchState.Error)
    }

    @Test
    fun fetchMedia_validUrl_successUpdatesPreview() = runTest {
        viewModel.onUrlChanged("https://www.instagram.com/reel/C123456/")
        viewModel.fetchMedia()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.fetchState is FetchState.Success)
        assertEquals("Test Reel", state.activePreviewMetadata?.title)
    }
}
