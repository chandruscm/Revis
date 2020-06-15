package com.servis.agora

import io.agora.rtm.RtmChannelAttribute
import io.agora.rtm.RtmChannelListener
import io.agora.rtm.RtmChannelMember
import io.agora.rtm.RtmFileMessage
import io.agora.rtm.RtmImageMessage
import io.agora.rtm.RtmMessage

open class BaseRtmChannelListener : RtmChannelListener {

    override fun onAttributesUpdated(list: MutableList<RtmChannelAttribute>) { }

    override fun onFileMessageReceived(fileMessage: RtmFileMessage, member: RtmChannelMember) { }

    override fun onImageMessageReceived(imageMessage: RtmImageMessage, member: RtmChannelMember) { }

    override fun onMessageReceived(message: RtmMessage, member: RtmChannelMember) { }

    override fun onMemberJoined(member: RtmChannelMember) { }

    override fun onMemberLeft(member: RtmChannelMember) { }

    override fun onMemberCountUpdated(i: Int) { }
}