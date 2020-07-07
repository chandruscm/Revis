package com.revis.ui.video

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.revis.ui.message.Message
import com.revis.ui.message.MessageChip
import com.revis.ui.message.Position
import com.revis.ui.video.VideoCallMode.VIDEO_NORMAL
import com.revis.ui.video.AnnotationState.ANNOTATION_CLEAR
import com.revis.ui.video.VideoCallMode.VIDEO_PAUSED
import com.revis.utils.VIDEO_QUALITY_HIGH
import com.revis.utils.flipBoolean
import com.revis.utils.notifyObserver
import io.agora.rtm.RtmChannelMember
import io.agora.rtm.RtmMessage
import javax.inject.Inject

class VideoCallViewModel @Inject constructor(
    private val gson: Gson
) : ViewModel() {

    var channel: String = ""

    val cameraState = MutableLiveData(false)
    val videoState = MutableLiveData(false)
    val micState = MutableLiveData(false)
    val messagesState = MutableLiveData(false)
    val settingsState = MutableLiveData(false)
    val speakerState = MutableLiveData(true)
    val currentVideoCallMode = MutableLiveData(VIDEO_NORMAL)
    val currentAnnotationState = MutableLiveData(ANNOTATION_CLEAR)

    val remoteUserJoined = MutableLiveData(false)

    val localPointerLocation = MutableLiveData(Position(0f, 0f))
    val remotePointerLocation = MutableLiveData(Position(0f, 0f))

    val localArrowLocation = MutableLiveData(Position(0f, 0f))
    val remoteArrowLocation = MutableLiveData(Position(0f, 0f))

    val remoteAnnotationClear = MutableLiveData(true)
    val localAnnotationClear = MutableLiveData(true)

    val remoteVideoDisabled = MutableLiveData(false)

    val messageList = MutableLiveData(arrayListOf<MessageChip>())

    val videoQualitySetting = MutableLiveData(VIDEO_QUALITY_HIGH)

    val actionBarHeight = MutableLiveData(0)

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

    fun createClearMessage(): String {
        return gson.toJson(Message(Message.Type.CLEAR))
    }

    fun createPauseMessage(): String {
        return gson.toJson(Message(Message.Type.PAUSE))
    }

    fun createResumeMessage(): String {
        return gson.toJson(Message(Message.Type.RESUME))
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
        currentVideoCallMode.value = VIDEO_NORMAL
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

    fun setRemotePointerLocation(position: Position) {
        remotePointerLocation.value = position
    }

    fun sendLocalPointerLocation(x: Float, y: Float) {
        Log.i("Video", "Sending local pointer loaction $x $y")
        localPointerLocation.value = Position(x, y)
    }

    fun setRemoteArrowLocation(position: Position) {
        remoteArrowLocation.value = position
    }

    fun sendLocalArrowLocation(x: Float, y: Float) {
        localArrowLocation.value = Position(x, y)
    }

    fun sendLocalAnnotationClear() {
        localAnnotationClear.flipBoolean()
    }

    fun clearRemoteAnnotation() {
        remoteAnnotationClear.flipBoolean()
    }

    fun pauseVideo() {
        currentVideoCallMode.value = VIDEO_PAUSED
    }

    fun resumeVideo() {
        currentVideoCallMode.value = VIDEO_NORMAL
    }
}