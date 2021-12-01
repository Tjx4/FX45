package com.platform45.fx45.ui.dashboard

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import xyz.appic.repositories.IFXRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DashboardViewModelTest {
    private lateinit var dashboardViewModel: DashboardViewModel
    @Mock
    private lateinit var mockApplication: Application
    @Mock
    private lateinit var fxRepository: IFXRepository

    private val dispatcher = TestCoroutineDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockitoAnnotations.openMocks(this)
        dashboardViewModel = DashboardViewModel(mockApplication, fxRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}