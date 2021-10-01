package com.platform45.fx45.ui.convert

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.platform45.fx45.R
import com.platform45.fx45.constants.API_KEY
import com.platform45.fx45.models.Conversion
import com.platform45.fx45.models.ResponseError
import com.platform45.fx45.repositories.IFXRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
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
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.openMocks

class ConversionViewModelTest {

    private lateinit var conversionViewModel: ConversionViewModel
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
        openMocks(this)
        conversionViewModel = ConversionViewModel(mockApplication, fxRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `error message should be displayed when the currency converting from is not specified`() {
        val errorMessage = "Add currency to convert from"
        val from = ""
        val to = "ZAR"
        val amount = "1"

        `when`(mockApplication.getString(R.string.from_convert_error)).thenReturn(errorMessage)
        conversionViewModel.checkAndConvert(from, to, amount)

        assertEquals(conversionViewModel.error.value, errorMessage)
    }

    @Test
    fun `error message should be displayed when the currency converting to is not specified`() {
        val errorMessage = "Add currency to convert from"
        val from = "USD"
        val to = ""
        val amount = "1"

        `when`(mockApplication.getString(R.string.to_convert_error)).thenReturn(errorMessage)
        conversionViewModel.checkAndConvert(from, to, amount)

        assertEquals(conversionViewModel.error.value, errorMessage)
    }

    @Test
    fun `check if to convert method is called`() = runBlocking {
        val errorMessage = "Add currency to convert from"
        val from = "USD"
        val to = "ZAR"
        val amount = "1"

        conversionViewModel.checkAndConvert(from, to, amount)

        assert(conversionViewModel.isValidInput.value == true)
    }

    @Test
    fun `check if the from amount is preset correctly`() = runBlocking {
        val from = "USD"
        val to = "ZAR"

        conversionViewModel.presetCurrencyPair(from, to)

        assertEquals(conversionViewModel.from.value, from)
    }

    @Test
    fun `check if the to amount is preset correctly`() {
        val from = "USD"
        val to = "ZAR"

        conversionViewModel.presetCurrencyPair(from, to)

        assertEquals(conversionViewModel.to.value, to)
    }


    @Test
    fun `check if error response handled`() = runBlockingTest {
        val from = "USD"
        val to = "ZAR"
        val amount = "1"
        val errorMessage = "Sorry an error occurred"

        `when`(conversionViewModel.fXRepository.getConversion(API_KEY, from, to, amount)).thenReturn(null)
        `when`(mockApplication.getString(R.string.convert_response_error)).thenReturn(errorMessage)
        conversionViewModel.convertCurrency(from, to, amount)

        assertEquals(conversionViewModel.dialogErrorMessage.value, errorMessage)
    }

    @Test
    fun `check error handled`() = runBlockingTest {
        val from = "USD"
        val to = "ZAR"
        val amount = "1"
        val error = ResponseError("202", "error message")
        val conversion = Conversion(0.0, 0, 0.0, from, to, error)

        `when`(conversionViewModel.fXRepository.getConversion(API_KEY, from, to, amount)).thenReturn(conversion)
        conversionViewModel.convertCurrency(from, to, amount)

        assertEquals(conversionViewModel.dialogErrorMessage.value, error.info)
    }

    @Test
    fun `check if converted to the correct amount`() = runBlockingTest {
        val from = "USD"
        val to = "ZAR"
        val amount = "1"
        val expectedConversion = Conversion(16.0, 1, 20.0, from, to)

        `when`(conversionViewModel.fXRepository.getConversion(API_KEY, from, to, amount)).thenReturn(expectedConversion)
        conversionViewModel.convertCurrency(from, to, amount)
        val actualConversion = conversionViewModel.conversion.value

        assertEquals(actualConversion, expectedConversion)
    }

}