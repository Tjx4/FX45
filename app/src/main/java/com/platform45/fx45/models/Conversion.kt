package com.platform45.fx45.models

import com.google.gson.annotations.SerializedName

data class Conversion(
    @SerializedName("price") var price: Double = 0.0,
    @SerializedName("timestamp") var timestamp: Int? = null,
    @SerializedName("total") var total: Double? = null,
    @SerializedName("from") var from: String? = null,
    @SerializedName("to") var to: String? = null,
) : ResponseError()