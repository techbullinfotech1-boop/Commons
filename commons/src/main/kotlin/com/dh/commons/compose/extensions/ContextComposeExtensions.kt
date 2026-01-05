package com.dh.commons.compose.extensions

import android.content.Context
import com.dh.commons.helpers.BaseConfig

val Context.config: BaseConfig get() = BaseConfig.newInstance(applicationContext)
