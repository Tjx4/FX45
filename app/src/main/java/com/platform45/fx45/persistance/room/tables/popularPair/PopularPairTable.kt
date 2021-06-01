package com.platform45.weather45.persistance.room.tables.popularPair

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "popularPairs")
data class PopularPairTable (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Long = 0L,
    @ColumnInfo(name ="pair")
    var pair: String?,
    @ColumnInfo(name ="fullName")
    var fullName: String?,
): Parcelable