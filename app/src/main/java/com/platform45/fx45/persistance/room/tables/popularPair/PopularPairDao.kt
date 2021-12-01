package com.platform45.fx45.persistance.room.tables.popularPair

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PopularPairDao {
    @Insert
    fun insert(popularPairTable: PopularPairTable)

    @Update
    fun update(popularPairTable: PopularPairTable)

    @Delete
    fun delete(popularPairTable: PopularPairTable)

    @Query("SELECT * FROM popularPairs WHERE id = :key")
    fun get(key: Long): PopularPairTable?

    @Query("SELECT * FROM popularPairs ORDER BY id DESC LIMIT 1")
    fun getLastUser(): PopularPairTable?

    @Query("SELECT * FROM popularPairs ORDER BY id DESC")
    fun getAllPairsLiveData(): LiveData<List<PopularPairTable>>

    @Query("SELECT * FROM popularPairs ORDER BY id DESC")
    fun getAllPairs():List<PopularPairTable>?

    @Query("DELETE  FROM popularPairs")
    fun clear()
}