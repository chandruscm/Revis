package com.revis.ui.message

import com.google.gson.annotations.SerializedName

/**
 * Gson model that can hold our custom messages.
 * - Primary constructor builds an Arrow Message.
 */
data class Message(
    @SerializedName("type") val type: Type,
    @SerializedName("body") val body: String?,
    @SerializedName("position") val position: Position?
) {
    // Text Message
    constructor(type: Type, body: String) : this(type, body, null)
    // Pointer Message
    constructor(type: Type, position: Position) : this(type, null, position)
    // Clear Message, Pause Message, Resume Message
    constructor(type: Type) : this(type, null, null)

    /**
     * Message Types
     */
    enum class Type {
        TEXT,
        POINTER,
        ARROW,
        CLEAR,
        PAUSE,
        RESUME
    }
}