package com.revis.ui.contacts

data class Contact(
    val id: Int,
    val name: String,
    val company: String,
    val image: Int,
    val status: Status
) {

    /**
     * Active status of the contact.
     */
    enum class Status {
        ONLINE,
        AWAY,
        BUSY
    }
}