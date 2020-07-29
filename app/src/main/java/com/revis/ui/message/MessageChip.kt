package com.revis.ui.message

import io.agora.rtm.RtmChannelMember

/**
 * Model for the RTM Message.
 */
data class MessageChip(
    val id: Int,
    val member: RtmChannelMember?,
    val text: String,
    val isSelf: Boolean
)