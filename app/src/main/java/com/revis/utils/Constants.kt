package com.revis.utils

import android.content.res.Resources
import android.graphics.BitmapFactory
import com.revis.R
import com.revis.ui.contacts.Contact
import com.revis.ui.contacts.Contact.Status.ONLINE
import com.revis.ui.contacts.Contact.Status.AWAY
import com.revis.ui.contacts.Contact.Status.BUSY
import com.revis.ui.contacts.CallLog
import com.revis.ui.contacts.CallLog.State.INCOMING
import com.revis.ui.contacts.CallLog.State.MISSED

const val IS_TECHNICAN = true

fun getSampleContacts(resources: Resources) = listOf(
    Contact(
        id = 1,
        name = "Marc Schumacher",
        company = "WR Kompressorenservice",
        image = R.drawable.contact_1,
        status = ONLINE
    ),
    Contact(
        id = 2,
        name = "Anastasiya Pavlova",
        company = "WR Kompressorenservice",
        image = R.drawable.contact_2,
        status = AWAY
    ),
    Contact(
        id = 3,
        name = "Joseph Gonzalez",
        company = "WR Kompressorenservice",
        image = R.drawable.contact_3,
        status = BUSY
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
            status = BUSY
        ),
        timeText = "10:30 am",
        state = INCOMING
    ),
    CallLog(
        id = 2,
        contact = Contact(
            id = 3,
            name = "Joseph Gonzalez",
            company = "WR Kompressorenservice",
            image = R.drawable.contact_3,
            status = BUSY
        ),
        timeText = "10:15 am",
        state = MISSED
    )
)
