package com.revis.ui.contacts

data class CallLog(
    val id: Int,
    val contact: Contact,
    val timeText: String,
    val state: State
) {

    /**
     * Active status of the contact.
     */
    enum class State {
        INCOMING,
        OUTGOING,
        MISSED
    }
}