package com.platform45.fx45.ui.tradeHistory

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.platform45.fx45.networking.retrofit.RetrofitHelper
import com.platform45.fx45.persistance.room.FX45Db
import com.platform45.fx45.repositories.FXRepository
import com.platform45.fx45.repositories.IFXRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.MockitoAnnotations.initMocks

class TradeHistoryViewModelTest {
    private lateinit var tradeHistoryViewModel: TradeHistoryViewModel
    @Mock
    private lateinit var mockApplication: Application
    @Mock
    private lateinit var fxRepository: IFXRepository

    @Mock
    lateinit var retrofitHelper: RetrofitHelper
    @Mock
    lateinit var database: FX45Db


    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        initMocks(this)
        tradeHistoryViewModel = TradeHistoryViewModel(mockApplication, fxRepository)
    }

    @Test
    fun `check if pairs list set`() {
        val currency1 = "ZAR"
        val currency2 = "USD"
        val pair = "$currency1,$currency2"

        tradeHistoryViewModel.setPairsList(pair)

        Assert.assertEquals(tradeHistoryViewModel.selectedPairs.value, pair.split(","))
    }

}