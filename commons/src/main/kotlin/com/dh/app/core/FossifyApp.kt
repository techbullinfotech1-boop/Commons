package com.dh.app.core

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.dh.app.core.extensions.appLockManager
import com.dh.app.core.extensions.checkUseEnglish

open class FossifyApp : Application() {

    open val isAppLockFeatureAvailable = false

    override fun onCreate() {
        super.onCreate()
        checkUseEnglish()
        setupAppLockManager()
    }

    private fun setupAppLockManager() {
        if (isAppLockFeatureAvailable) {
            ProcessLifecycleOwner.get().lifecycle.addObserver(appLockManager)
        }
    }
}
