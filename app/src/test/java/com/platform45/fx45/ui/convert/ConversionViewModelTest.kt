package com.platform45.fx45.ui.convert

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.platform45.fx45.constants.API_KEY
import com.platform45.fx45.models.Conversion
import com.platform45.fx45.repositories.IFXRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.openMocks

class ConversionViewModelTest {

    private lateinit var conversionViewModel: ConversionViewModel
    @Mock
    private lateinit var mockApplication: Application
    @Mock
    private lateinit var fxRepository: IFXRepository

    val dispatcher = TestCoroutineDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        openMocks(this)
        conversionViewModel = ConversionViewModel(mockApplication, fxRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test preset from`() = runBlocking {
        val from = "USD"
        val to = "ZAR"

        conversionViewModel.presetCurrencies(from, to)

        assertEquals(conversionViewModel.from.value, from)
    }

    @Test
    fun `test preset to`() = runBlocking {
        val from = "USD"
        val to = "ZAR"

        conversionViewModel.presetCurrencies(from, to)

        assertEquals(conversionViewModel.to.value, to)
    }

    @Test
    fun `test convertion`() = runBlocking {
        val from = "USD"
        val to = "ZAR"
        val amount = "1"
        val conversion = Conversion(16.0, 1, 20.0, from, to)

        `when`(conversionViewModel.fXRepository.getConversion(API_KEY, from, to, amount)).thenReturn(conversion)
        conversionViewModel.convertCurrency(from, to, amount)

        assertEquals(conversionViewModel.convert.value, conversion)
    }

}