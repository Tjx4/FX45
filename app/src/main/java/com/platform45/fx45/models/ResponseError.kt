package com.platform45.fx45.models

import com.google.gson.annotations.SerializedName

open class ResponseError(
    @SerializedName("code") var code: String? = null,
    @SerializedName("info") var info: String? = null
)