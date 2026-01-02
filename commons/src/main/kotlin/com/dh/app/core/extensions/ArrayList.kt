package com.dh.app.core.extensions

fun <T> ArrayList<T>.moveLastItemToFront() {
    val last = removeAt(size - 1)
    add(0, last)
}
