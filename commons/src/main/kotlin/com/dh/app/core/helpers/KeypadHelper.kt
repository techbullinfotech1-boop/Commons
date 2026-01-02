package com.dh.app.core.helpers

import android.telephony.PhoneNumberUtils


object KeypadHelper {
    // this should probably be split into language-specific mappings
    private val KEYPAD_MAP = mutableMapOf<Char, Int>().apply {
        put('ı', 4)
        put('İ', 4)

        put('ł', 5)
        put('Ł', 5)
    }


    fun convertKeypadLettersToDigits(input: String): String {
        val digits = PhoneNumberUtils.convertKeypadLettersToDigits(input)
        val result = StringBuilder(digits.length)
        for (c in digits) {
            val digit = KEYPAD_MAP[c] ?: c
            result.append(digit)
        }

        return result.toString()
    }
}
