package com.dh.app.core.interfaces

import androidx.biometric.auth.AuthPromptHost
import com.dh.app.core.views.MyScrollView

interface SecurityTab {
    fun initTab(
        requiredHash: String,
        listener: HashListener,
        scrollView: MyScrollView?,
        biometricPromptHost: AuthPromptHost,
        showBiometricAuthentication: Boolean
    )

    fun visibilityChanged(isVisible: Boolean)
}
