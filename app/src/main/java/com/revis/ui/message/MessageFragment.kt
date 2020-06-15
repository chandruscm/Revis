package com.revis.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.revis.agora.BaseRtmChannelListener
import com.revis.agora.BaseRtmClient
import com.revis.agora.BaseRtmClientListener
import com.revis.databinding.FragmentMessageBinding
import com.revis.ui.shared.BaseFragment
import com.revis.utils.showToast
import io.agora.rtm.ErrorInfo
import io.agora.rtm.ResultCallback
import io.agora.rtm.RtmChannel
import io.agora.rtm.RtmChannelMember
import io.agora.rtm.RtmClient
import io.agora.rtm.RtmMessage
import io.agora.rtm.RtmStatusCode
import io.agora.rtm.SendMessageOptions
import java.util.UUID
import javax.inject.Inject

class MessageFragment : BaseFragment() {

    private lateinit var binding: FragmentMessageBinding

    private val args: MessageFragmentArgs by navArgs()

    @Inject
    lateinit var rtmClientFactory: BaseRtmClient.Factory

    @Inject
    lateinit var sendMessageOptions: SendMessageOptions

    private val rtmClientListener = object : BaseRtmClientListener() { }

    private val rtmChannelListener = object : BaseRtmChannelListener() {

        override fun onMessageReceived(message: RtmMessage, member: RtmChannelMember) {
            requireActivity().runOnUiThread { updateMessages(message.text, member, false) }
        }
    }

    private var rtmClient: RtmClient? = null

    private var rtmChannel: RtmChannel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initAgora()
        initListeners()
    }

    private fun initAgora() {
        rtmClient = rtmClientFactory.create(rtmClientListener)
        val randomUUID = UUID.randomUUID().toString().substring(0,6)
        rtmClient?.login(null, randomUUID, object : ResultCallback<Void?> {
            
            override fun onSuccess(responseInfo: Void?) {
                joinChannel()
            }

            override fun onFailure(errorInfo: ErrorInfo) {
                requireActivity().runOnUiThread {
                    showToast("Login failed")
                }
            }
        })
    }

    private fun initListeners() {
        binding.buttonSendMessage.setOnClickListener {
            binding.messageInput.text.toString().let { message ->
                if (!message.isEmpty()) {
                    sendMessage(message)
                }
            }
        }
    }

    private fun joinChannel() {
        rtmChannel = rtmClient?.createChannel(
            args.channel,
            rtmChannelListener
        )

        rtmChannel?.join(object : ResultCallback<Void?> {
            override fun onSuccess(responseInfo: Void?) {
                requireActivity().runOnUiThread {
                    showToast("Join channel success")
                }
            }

            override fun onFailure(errorInfo: ErrorInfo) {
                requireActivity().runOnUiThread {
                    showToast("Failed to join channel")
                }
            }
        })
    }

    private fun leaveAndReleaseChannel() {
        rtmChannel?.apply {
            leave(null)
            release()
        }
    }

    private fun verifyConnection(state: Int, reason: Int) {
        when (state) {
            RtmStatusCode.ConnectionState.CONNECTION_STATE_RECONNECTING -> showToast("Reconnecting")
            RtmStatusCode.ConnectionState.CONNECTION_STATE_ABORTED -> showToast("Connection Lost")
        }
    }

    private fun updateMessages(message: String, member: RtmChannelMember? = null, isSelf: Boolean = false) {
        binding.messages.append("\n ${if (isSelf) "You" else member?.userId}: $message")
    }

    private fun clearMessageInput() = binding.messageInput.text.clear()

    private fun sendMessage(message: String) {
        rtmChannel?.sendMessage(rtmClient?.createMessage(message), sendMessageOptions,
            object : ResultCallback<Void?> {

                override fun onSuccess(responseInfo: Void?) {
                    requireActivity().runOnUiThread {
                        updateMessages(message, isSelf = true)
                        clearMessageInput()
                    }
                }

                override fun onFailure(errorInfo: ErrorInfo) {
                    requireActivity().runOnUiThread {
                        when (errorInfo.errorCode) {
                            RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_TIMEOUT,
                            RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_FAILURE ->
                                showToast("Failed to send message")
                        }
                    }
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        rtmClient?.logout(null)
        leaveAndReleaseChannel()
        rtmChannel = null
    }
}
