package com.revis.ui.contacts

/**
 * Model for a Contact.
 */
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