package com.dh.commons.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dh.commons.extensions.syncGlobalConfig
import com.dh.commons.helpers.MyContentProvider

class FossifyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == MyContentProvider.ACTION_GLOBAL_CONFIG_UPDATED) {
            context?.syncGlobalConfig()
        }
    }
}
