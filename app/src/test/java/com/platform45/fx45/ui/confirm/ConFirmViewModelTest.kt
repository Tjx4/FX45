package com.platform45.fx45.ui.confirm

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.platform45.fx45.helpers.getClosestWeekDay
import com.platform45.fx45.repositories.IFXRepository
import com.platform45.fx45.ui.conFirm.DashboardViewModel
import junit.framework.Assert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ConFirmViewModelTest{
    private lateinit var conFirmViewModel: ConFirmViewModel
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
        conFirmViewModel = ConFirmViewModel(mockApplication, fxRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `check if currencies are set`() = runBlockingTest {
        conFirmViewModel.initCurrencies()

        assert(!conFirmViewModel.availableCurrencies.value.isNullOrEmpty())
    }

    @Test
    fun `start date should be initialized`() = runBlockingTest {
        val wd = getClosestWeekDay(30)

        conFirmViewModel.initStartAndEndDate()

        Assert.assertEquals(conFirmViewModel.startDate.value, wd)
    }

    @Test
    fun `currency pairs should be set`() = runBlockingTest {
        val frmIndx = 1
        val toIndx = 2
        val currencyPais = arrayListOf("ZAR", "USD", "EUR")
        val expectedPair = "${currencyPais[frmIndx]}${currencyPais[toIndx]}"

        conFirmViewModel.initCurrencies()
        conFirmViewModel.setCurrencyPair(frmIndx, toIndx)
        val actualPair = conFirmViewModel.userSelectedPair.value

        Assert.assertEquals(actualPair, expectedPair)
    }
}