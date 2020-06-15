package com.revis.agora

import android.content.Context
import android.util.Log
import io.agora.rtm.RtmClient
import io.agora.rtm.RtmClientListener
import java.lang.Exception
import java.lang.RuntimeException

/**
 * Using factory pattern to become compatible with Dagger.
 */
abstract class BaseRtmClient : RtmClient() {
    class Factory(
        private val context: Context,
        private val appId: String
    ) {
        fun create(listener: RtmClientListener): RtmClient {
            try {
                return createInstance(context, appId, listener)
            } catch (e: Exception) {
                throw RuntimeException(
                    "NEED TO check rtm sdk init fatal error\n" +
                            Log.getStackTraceString(e)
                )
            }
        }
    }
}