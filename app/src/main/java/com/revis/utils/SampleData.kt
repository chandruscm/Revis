package com.revis.utils

import android.content.res.Resources
import com.revis.R
import com.revis.ui.contacts.CallLog
import com.revis.ui.contacts.Contact
import com.revis.ui.message.MessageChip

fun getSampleContacts(resources: Resources) = listOf(
    Contact(
        id = 1,
        name = "Marc Schumacher",
        company = "WR Kompressorenservice",
        image = R.drawable.contact_1,
        status = Contact.Status.ONLINE
    ),
    Contact(
        id = 2,
        name = "Anastasiya Pavlova",
        company = "WR Kompressorenservice",
        image = R.drawable.contact_2,
        status = Contact.Status.AWAY
    ),
    Contact(
        id = 3,
        name = "Joseph Gonzalez",
        company = "WR Kompressorenservice",
        image = R.drawable.contact_3,
        status = Contact.Status.BUSY
    )
)

fun getSampleInvitees(resources: Resources) = listOf(
    Contact(
        id = 1,
        name = "Marc Schumacher",
        company = "WR Kompressorenservice",
        image = R.drawable.contact_1,
        status = Contact.Status.ONLINE
    ),
    Contact(
        id = 2,
        name = "Anastasiya Pavlova",
        company = "WR Kompressorenservice",
        image = R.drawable.contact_2,
        status = Contact.Status.AWAY
    )
)

fun getSampleCallLogs(resources: Resources) = listOf(
    CallLog(
        id = 1,
        contact = Contact(
            id = 3,
            name = "Joseph Gonzalez",
            company = "WR Kompressorenservice",
            image = R.drawable.contact_3,
            status = Contact.Status.BUSY
        ),
        timeText = "10:30 am",
        state = CallLog.State.INCOMING
    ),
    CallLog(
        id = 2,
        contact = Contact(
            id = 3,
            name = "Joseph Gonzalez",
            company = "WR Kompressorenservice",
            image = R.drawable.contact_3,
            status = Contact.Status.BUSY
        ),
        timeText = "10:15 am",
        state = CallLog.State.MISSED
    )
)

fun getSampleMessages() = arrayListOf(
    MessageChip(1, null, "Engine number : 8716481", true),
    MessageChip(2, null, "V8 Superleggera engine oil", false),
    MessageChip(1, null, "That's expensive AF!", true),
    MessageChip(1, null, "Who told you to get a lamborghini, Lol!", false)
)