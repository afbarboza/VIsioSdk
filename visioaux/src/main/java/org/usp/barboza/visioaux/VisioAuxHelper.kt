package org.usp.barboza.visioaux

import android.util.Log
import android.view.accessibility.AccessibilityEvent

object VisioAuxHelper {
    private val ACCESSIBILITY_TAG = "VisioAuxLog"

    fun shouldIgnoreEvent(accessibilityEvent: AccessibilityEvent?): Boolean {
        return accessibilityEvent?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
    }

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