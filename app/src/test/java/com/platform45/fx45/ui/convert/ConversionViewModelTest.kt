package com.platform45.fx45.ui.convert

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.platform45.fx45.R
import com.platform45.fx45.constants.API_KEY
import com.platform45.fx45.models.Conversion
import com.platform45.fx45.repositories.IFXRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.openMocks
import retrofit2.Response

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
    fun `check if right message displayed when From currency not set`() {
        val errorMessage = "Add currency to convert from"
        val from = ""
        val to = "ZAR"
        val amount = "1"

        `when`(mockApplication.getString(R.string.from_convert_error)).thenReturn(errorMessage)
        conversionViewModel.checkAndConvert(from, to, amount)

        assertEquals(conversionViewModel.errorMessage.value, errorMessage)
    }

    @Test
    fun `check if to currency not set`() {
        val errorMessage = "Add currency to convert from"
        val from = "USD"
        val to = ""
        val amount = "1"

        conversionViewModel.checkAndConvert(from, to, amount)

        assertEquals(conversionViewModel.errorMessage.value, errorMessage)
    }

    @Test
    fun `check if to convert is called`() = runBlocking {
        val errorMessage = "Add currency to convert from"
        val from = "USD"
        val to = "ZAR"
        val amount = "1"

        conversionViewModel.checkAndConvert(from, to, amount)

        verify(conversionViewModel, times(1)).convertCurrency(from, to, amount)
    }

    @Test
    fun `check if the from amount is preset correctly`() = runBlocking {
        val from = "USD"
        val to = "ZAR"

        conversionViewModel.presetCurrencies(from, to)

        assertEquals(conversionViewModel.from.value, from)
    }

    @Test
    fun `check if the to amount is preset correctly`() = runBlocking {
        val from = "USD"
        val to = "ZAR"

        conversionViewModel.presetCurrencies(from, to)

        assertEquals(conversionViewModel.to.value, to)
    }

    @Test
    fun `check if amount is converted to the correct amount`() = runBlockingTest {
        val from = "USD"
        val to = "ZAR"
        val amount = "1"
        val conversion = Conversion(16.0, 1, 20.0, from, to)
        val successResponse: Response<Conversion?> = Response.success(conversion)

        `when`(conversionViewModel.fXRepository.getConversion(API_KEY, from, to, amount)).thenReturn(successResponse)
        conversionViewModel.convertCurrency(from, to, amount)

        assertEquals(conversionViewModel.convert.value, conversion)
    }

 /*
    @Test
    fun `check if amount is convertion error`() = runBlockingTest {
        val from = "USD"
        val to = "ZAR"
        val amount = "1"
        val conversion = Conversion(16.0, 1, 20.0, from, to)
        val errorResponse = "{\n " +
                "\"type\": \"error\", \n " +
                "\"message\": \"Some error message.\" \n" +
                "}"
        val responseBody = errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
        val errorResponseBody = Response.error<Conversion>(400, responseBody)

        `when`(conversionViewModel.fXRepository.getConversion(API_KEY, from, to, amount)).thenReturn(errorResponseBody)
        conversionViewModel.convertCurrency(from, to, amount)

        assertEquals(conversionViewModel.dialogErrorMessage.value , errorResponseBody.errorBody().string())
    }
*/
}