package com.revis.ui.video

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.revis.ui.message.Message
import com.revis.ui.message.Position
import com.revis.utils.flipBoolean
import io.agora.rtm.RtmMessage
import javax.inject.Inject

class VideoCallViewModel @Inject constructor(
    private val gson: Gson
) : ViewModel() {

    val cameraState = MutableLiveData(false)
    val videoState = MutableLiveData(false)
    val micState = MutableLiveData(false)
    val messagesState = MutableLiveData(false)
    val pointerLocation = MutableLiveData(Position(0f, 0f))

    fun createTextMessage(message: String): String {
        return gson.toJson(Message(Message.Type.TEXT, message))
    }

    fun createPointerMessage(position: Position): String {
        return gson.toJson(Message(Message.Type.POINTER, position))
    }

    fun createArrowMessage(step: String, position: Position): String {
        return gson.toJson(Message(Message.Type.ARROW, step, position))
    }

    fun getMessageFromRtm(rtmMessage: RtmMessage): Message {
        return gson.fromJson(rtmMessage.text, Message::class.java)
    }

    fun toggleCamera() = cameraState.flipBoolean()

    fun toggleVideo() = videoState.flipBoolean()

    fun toggleMic() = micState.flipBoolean()

    fun toggleMessages() = messagesState.flipBoolean()

    fun movePointer(x: Float, y: Float) {
        pointerLocation.value = Position(x, y)
    }

    fun movePointer(position: Position) {
        pointerLocation.value = position
    }
}