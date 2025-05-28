package org.usp.barboza.visioaux

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.view.accessibility.AccessibilityManager

object VisioAuxServiceChecker {
    private val VISIOAUX_SERVICE_ID = "org.usp.barboza.visioaux/.VisioAuxService"

    fun isVisioAuxRuntimePermissionGranted(context: Context): Boolean {
        val accesibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledAccessibilityServices = accesibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)

        for (service in enabledAccessibilityServices) {
            if (service.id.equals(VISIOAUX_SERVICE_ID)) {
                return true
            }
        }

        return false
    }
}