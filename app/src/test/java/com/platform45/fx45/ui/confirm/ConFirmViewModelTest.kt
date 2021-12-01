package com.platform45.fx45.ui.confirm

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import xyz.appic.common.helpers.getClosestWeekDay
import xyz.appic.repositories.IFXRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
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
    fun `start date should be initialized`() {
        val weekday = getClosestWeekDay(30)

        conFirmViewModel.initStartAndEndDate()

        assertEquals(conFirmViewModel.startDate.value, weekday)
    }

    @Test
    fun `check if currencies are set`()  {
        conFirmViewModel.initCurrencies()

        assert(!conFirmViewModel.availableCurrencies.value.isNullOrEmpty())
    }

    @Test
    fun `currency pairs should be set`()  {
        val frmIndx = 1
        val toIndx = 2
        val currencyPais = arrayListOf("ZAR", "USD", "EUR")
        val expectedPair = "${currencyPais[frmIndx]}${currencyPais[toIndx]}"

        conFirmViewModel.setCurrencyPair(frmIndx, toIndx)
        val actualPair = conFirmViewModel.userSelectedPair.value

        assertEquals(actualPair, expectedPair)
    }

}