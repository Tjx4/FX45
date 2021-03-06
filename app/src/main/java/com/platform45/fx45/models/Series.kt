package com.platform45.fx45.models

import com.google.gson.annotations.SerializedName

data class Series(
    @SerializedName("end_date") var endDate: String?,
    @SerializedName("start_date") var startDate: String?,
    @SerializedName("price") var price: Object?,
    @SerializedName("error") var error: ResponseError?
)
