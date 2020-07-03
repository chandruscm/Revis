package com.revis.ui.message

import io.agora.rtm.RtmChannelMember

data class MessageChip(
    val id: Int,
    val member: RtmChannelMember?,
    val text: String,
    val isSelf: Boolean
)