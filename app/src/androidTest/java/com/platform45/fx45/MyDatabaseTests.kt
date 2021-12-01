package com.platform45.fx45

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import xyz.appic.core.persistance.room.FX45Db
import xyz.appic.core.persistance.room.tables.pairHistory.PairHistoryDAO
import xyz.appic.core.persistance.room.tables.pairHistory.PairHistoryTable
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MyDatabaseTests{
    private lateinit var pairHistoryDAO: PairHistoryDAO
    private lateinit var fx45Db: FX45Db

    @Before
    fun createDB(){
        var context = InstrumentationRegistry.getInstrumentation().targetContext

        fx45Db = Room.inMemoryDatabaseBuilder(context, FX45Db::class.java)
            .allowMainThreadQueries()
            .build()

        pairHistoryDAO = fx45Db.pairHistoryDAO
    }

    @After
    @Throws(IOException::class)
    fun closeDB(){
        fx45Db.close()
    }

    @Test
    @Throws(Exception::class)
    fun check_if_currency_pair_successfully_added_to_DB(){
        var pairHistoryTable = PairHistoryTable(1, "", "", "", "")

        pairHistoryDAO.insert(pairHistoryTable)
        val actualPairHistoryTable = pairHistoryDAO.get(pairHistoryTable.id)

       assertEquals(actualPairHistoryTable?.id, pairHistoryTable.id)
    }

}