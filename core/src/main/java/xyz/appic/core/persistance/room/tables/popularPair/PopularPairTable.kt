package xyz.appic.core.persistance.room.tables.popularPair

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "popularPairs")
data class PopularPairTable (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Long = 0L,
    @ColumnInfo(name ="pair")
    var pair: String?,
    @ColumnInfo(name ="fullName")
    var fullName: String?,
)