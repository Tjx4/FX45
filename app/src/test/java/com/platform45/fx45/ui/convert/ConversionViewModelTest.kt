package com.platform45.fx45.ui.convert

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.platform45.fx45.constants.API_KEY
import com.platform45.fx45.models.Conversion
import com.platform45.fx45.repositories.IFXRepository
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ConversionViewModelTest {

    private lateinit var conversionViewModel: ConversionViewModel
    @Mock
    private lateinit var mockApplication: Application
    @Mock
    private lateinit var fxRepository: IFXRepository

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        conversionViewModel = ConversionViewModel(mockApplication, fxRepository)
    }

    @Test
    fun `test `() = runBlockingTest {
        val from = "USD"
        val to = "ZAR"
        val amount = "1"
        val conversion = Conversion(16.0, 1, 20.0, from, to)

        Mockito.`when`(conversionViewModel.fXRepository.getConversion(API_KEY, from, to, amount)).thenReturn(conversion)
        conversionViewModel.convertCurrency(from, to, amount)

        Assert.assertEquals(conversionViewModel.convert.value, conversion)
    }

}