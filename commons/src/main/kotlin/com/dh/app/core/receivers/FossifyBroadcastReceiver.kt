package com.dh.app.core.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dh.app.core.extensions.syncGlobalConfig
import com.dh.app.core.helpers.MyContentProvider

class FossifyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == MyContentProvider.ACTION_GLOBAL_CONFIG_UPDATED) {
            context?.syncGlobalConfig()
        }
    }
}
