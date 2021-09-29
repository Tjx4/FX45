package com.platform45.fx45.ui.dashboard

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.platform45.fx45.R
import com.platform45.fx45.helpers.getClosestWeekDay
import com.platform45.fx45.repositories.IFXRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
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

    @Test
    fun `check if currencies are set`() = runBlockingTest {
        dashboardViewModel.initCurrencies()

        assert(!dashboardViewModel.availableCurrencies.value.isNullOrEmpty())
    }

    @Test
    fun `start date should be initialized`() = runBlockingTest {
       val wd = getClosestWeekDay(30)

        dashboardViewModel.initStartAndEndDate()

        assertEquals(dashboardViewModel.startDate.value, wd)
    }

    @Test
    fun `currency pairs should be set`() = runBlockingTest {
        val frmIndx = 1
        val toIndx = 2
        val currencyPais = arrayListOf("ZAR", "USD", "EUR")
        val expectedPair = "${currencyPais[frmIndx]}${currencyPais[toIndx]}"

        dashboardViewModel.initCurrencies()
        dashboardViewModel.setCurrencyPair(frmIndx, toIndx)
        val actualPair = dashboardViewModel.userSelectedPair.value

        assertEquals(actualPair, expectedPair)
    }
}