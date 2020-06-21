package com.revis.ui.message

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import javax.inject.Inject
import com.revis.ui.message.Message.Type.TEXT
import com.revis.ui.message.Message.Type.POINTER
import com.revis.ui.message.Message.Type.ARROW
import com.revis.ui.message.Message.Type.CLEAR
import io.agora.rtm.RtmMessage

class MessageViewModel @Inject constructor(
    private val gson: Gson
) : ViewModel() {

    fun createTextMessage(message: String): String {
        return gson.toJson(Message(TEXT, message))
    }

    fun createPointerMessage(x: Int, y: Int): String {
        return gson.toJson(Message(POINTER, Position(x, y)))
    }

    fun createArrowMessage(step: String, x: Int, y: Int): String {
        return gson.toJson(Message(ARROW, step, Position(x, y)))
    }

    fun getMessageFromRtm(rtmMessage: RtmMessage): Message {
        return gson.fromJson(rtmMessage.text, Message::class.java)
    }
}