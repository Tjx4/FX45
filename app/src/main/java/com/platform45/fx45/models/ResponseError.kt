package com.platform45.fx45.models

import com.google.gson.annotations.SerializedName

data class ResponseError(
    @SerializedName("code") var code: String?,
    @SerializedName("info") var info: String?
)
