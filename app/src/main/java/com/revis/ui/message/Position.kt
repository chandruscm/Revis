package com.revis.ui.message

import com.google.gson.annotations.SerializedName

/**
 * Model to hold position data for the annotation.
 */
data class Position(
    @SerializedName("x") val x: Float,
    @SerializedName("y") val y: Float
)