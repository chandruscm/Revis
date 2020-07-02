package com.revis.agora

import com.google.gson.Gson
import com.revis.ui.message.Message
import io.agora.rtm.RtmChannelAttribute
import io.agora.rtm.RtmChannelListener
import io.agora.rtm.RtmChannelMember
import io.agora.rtm.RtmFileMessage
import io.agora.rtm.RtmImageMessage
import io.agora.rtm.RtmMessage
import com.revis.ui.message.Message.Type.TEXT
import com.revis.ui.message.Message.Type.POINTER
import com.revis.ui.message.Message.Type.ARROW
import com.revis.ui.message.Message.Type.CLEAR
import com.revis.ui.message.Position

/**
 * Create own callbacks for custom message types.
 */
open class BaseRtmChannelListener : RtmChannelListener {

    open fun onTextMessageReceived(text: String, member: RtmChannelMember) { }

    open fun onPointerMessageReceived(position: Position) { }

    open fun onArrowMessageReceived(step: String, position: Position) { }

    open fun onClearMessageReceived() { }

    override fun onAttributesUpdated(list: MutableList<RtmChannelAttribute>) { }

    override fun onFileMessageReceived(fileMessage: RtmFileMessage, member: RtmChannelMember) { }

    override fun onImageMessageReceived(imageMessage: RtmImageMessage, member: RtmChannelMember) { }

    override fun onMessageReceived(rtmMessage: RtmMessage, member: RtmChannelMember) {
        // Gson instance can be provided through Dagger.
        Gson().fromJson(rtmMessage.text, Message::class.java).let { message ->
            when (message.type) {
                TEXT -> onTextMessageReceived(message.body!!, member)
                POINTER -> onPointerMessageReceived(message.position!!)
                ARROW -> onArrowMessageReceived(message.body!!, message.position!!)
                CLEAR -> onClearMessageReceived()
            }
        }
    }

    override fun onMemberJoined(member: RtmChannelMember) { }

    override fun onMemberLeft(member: RtmChannelMember) { }

    override fun onMemberCountUpdated(i: Int) { }
}