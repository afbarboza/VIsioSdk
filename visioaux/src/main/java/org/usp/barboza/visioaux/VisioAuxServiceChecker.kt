package org.usp.barboza.visioaux

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.view.accessibility.AccessibilityManager
import org.usp.barboza.visioaux.VisioAuxHelper.debugLog

object VisioAuxServiceChecker {
    /**
     * Use this id when running the demo app.
     */
    private val VISIOAUX_SERVICE_ID_DEMO = "org.usp.barboza.visioaux/.VisioAuxService"
    private val VISIOAUX_SERVICE_ID = "org.usp.barboza.visioaux.VisioAuxService"

    fun isVisioAuxRuntimePermissionGranted(context: Context): Boolean {
        val packageName = context.packageName
        val visioAuxServiceId = "$packageName/$VISIOAUX_SERVICE_ID"
        val accesibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledAccessibilityServices = accesibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)

        for (service in enabledAccessibilityServices) {
            if (service.id.equals(visioAuxServiceId)) {
                return true
            }
        }

        return false
    }
}