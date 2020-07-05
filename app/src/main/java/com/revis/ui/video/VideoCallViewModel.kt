package com.revis.ui.video

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.revis.ui.message.Message
import com.revis.ui.message.MessageChip
import com.revis.ui.message.Position
import com.revis.ui.video.VideoCallState.VIDEO_NORMAL
import com.revis.ui.video.AnnotationState.ANNOTATION_CLEAR
import com.revis.ui.video.AnnotationState.ANNOTATION_POINTER
import com.revis.utils.VIDEO_QUALITY_HIGH
import com.revis.utils.flipBoolean
import com.revis.utils.notifyObserver
import io.agora.rtm.RtmChannelMember
import io.agora.rtm.RtmMessage
import javax.inject.Inject

class VideoCallViewModel @Inject constructor(
    private val gson: Gson
) : ViewModel() {

    val cameraState = MutableLiveData(false)
    val videoState = MutableLiveData(false)
    val micState = MutableLiveData(false)
    val messagesState = MutableLiveData(false)
    val settingsState = MutableLiveData(false)
    val speakerState = MutableLiveData(true)
    var currentVideoCallState = MutableLiveData(VIDEO_NORMAL)
    var currentAnnotationState = MutableLiveData(ANNOTATION_CLEAR)

    val pointerLocation = MutableLiveData(Position(0f, 0f))

    val messageList = MutableLiveData(arrayListOf<MessageChip>())

    val videoQualitySetting = MutableLiveData(VIDEO_QUALITY_HIGH)

    init {
//        messageList.value = getSampleMessages()
    }

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

    fun addNewMessage(message: String, member: RtmChannelMember? = null, isSelf: Boolean) {
        Log.i("VideoCall", "Adding message $message")
        messageList.value?.add(
            MessageChip(
                System.currentTimeMillis().toInt(),
                member,
                message,
                isSelf
            )
        )
        messageList.notifyObserver()
        messageList.value?.forEach { item ->
            Log.i("VideoCall", "List element : $item")
        }
    }

    fun resetState() {
        currentVideoCallState.value = VIDEO_NORMAL
    }

    fun toggleState(state: MutableLiveData<Boolean>) = state.flipBoolean()

    fun enableSettings() {
        settingsState.value = true
    }

    fun disableSettings() {
        settingsState.value = false
    }

    fun setAnnotationState(state: AnnotationState) {
        Log.i("Video", "Annotation set to ${state.name}")
        currentAnnotationState.value = state
    }

    fun setVideoQualitySetting(videoQualitySetting: Int) {
        this.videoQualitySetting.value = videoQualitySetting
    }

    fun movePointer(x: Float, y: Float) {
        pointerLocation.value = Position(x, y)
    }

    fun movePointer(position: Position) {
        pointerLocation.value = position
    }
}