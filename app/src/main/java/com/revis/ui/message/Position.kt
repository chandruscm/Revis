package com.revis.ui.message

import com.google.gson.annotations.SerializedName

data class Position(
    @SerializedName("x") val x: Float,
    @SerializedName("y") val y: Float
)