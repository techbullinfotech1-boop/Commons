package com.dh.app.core.extensions

import android.content.Context
import com.dh.app.core.models.FileDirItem

fun FileDirItem.isRecycleBinPath(context: Context): Boolean {
    return path.startsWith(context.recycleBinPath)
}
