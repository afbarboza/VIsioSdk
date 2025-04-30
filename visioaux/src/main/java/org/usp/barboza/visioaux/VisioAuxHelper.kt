package org.usp.barboza.visioaux

import android.util.Log

object VisioAuxHelper {
    private val ACCESSIBILITY_TAG = "VisioAuxLog"

    fun debugLog(message: String) {
        if (!BuildConfig.DEBUG) {
            return
        }

        Log.println(Log.DEBUG, ACCESSIBILITY_TAG, message)
    }

    fun accessibilityLog(message: String) {
        Log.println(Log.ERROR, ACCESSIBILITY_TAG, message)
    }
}