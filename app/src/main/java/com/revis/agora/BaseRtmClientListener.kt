package com.revis.agora

import io.agora.rtm.RtmClientListener
import io.agora.rtm.RtmImageMessage
import io.agora.rtm.RtmMediaOperationProgress
import io.agora.rtm.RtmMessage
import io.agora.rtm.RtmFileMessage

open class BaseRtmClientListener : RtmClientListener {

    override fun onMediaDownloadingProgress(mediaOperationProgress: RtmMediaOperationProgress, l: Long) { }

    override fun onTokenExpired() { }

    override fun onPeersOnlineStatusChanged(map: MutableMap<String, Int>) { }

    override fun onMediaUploadingProgress(mediaOperationProgress: RtmMediaOperationProgress, l: Long) { }

    override fun onImageMessageReceivedFromPeer(imageMessage: RtmImageMessage, peerId: String) { }

    override fun onConnectionStateChanged(state: Int, reason: Int) { }

    override fun onMessageReceived(message: RtmMessage, peerId: String) { }

    override fun onFileMessageReceivedFromPeer(fileMessage: RtmFileMessage, peerId: String) { }
}