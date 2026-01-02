package com.dh.app.core.compose.extensions

import android.content.Context
import com.dh.app.core.helpers.BaseConfig

val Context.config: BaseConfig get() = BaseConfig.newInstance(applicationContext)
