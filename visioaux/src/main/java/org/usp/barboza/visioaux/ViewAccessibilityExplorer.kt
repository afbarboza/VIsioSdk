package org.usp.barboza.visioaux

import android.view.View
import com.google.android.apps.common.testing.accessibility.framework.integrations.visioaux.VisioAuxAccessibilityValidator
import com.google.android.apps.common.testing.accessibility.framework.integrations.visioaux.VisioAuxLogReport
import org.usp.barboza.visioaux.VisioAuxHelper.accessibilityLog
import org.usp.barboza.visioaux.VisioAuxHelper.debugLog
import java.util.Hashtable
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString


object ViewAccessibilityExplorer {
    private val exploredViews = Hashtable<View, Boolean>(200)

    fun collectAccessibilityReport(view: View) {
        /**
         * WARNING: THIS IS A REALLY BAD HEURISTIC TO AVOID STATE EXPLOSION. THIS SHALL BE IMPROVED
         */
        if (viewWasAlreadyExplored(view)) {
            debugLog(">>> View marked as explored. Returning...")
            return
        }
        markViewAsExplored(view)

        VisioAuxAccessibilityValidator()
            .check(view)

        val checks = VisioAuxLogReport
            .getInstance()
            .reports

        val deviceId = VisioAuxViewListener.deviceId
        val activityName = VisioAuxViewListener.activityName

        accessibilityLog("Found ${checks.size} errors or warnings in the app for the current view")
        for (check in checks) {
            debugLog(">>> (${deviceId ?: "null"}) $activityName: ${check.check.category}: ${check.violationLogMessage} ")

            /* TODO fill in those info here */
            val violationType = check.check.category.toString()
            val developerMessage = check.violationLogMessage
            val conformanceLevel = check.check.conformanceLevel

            val newViolation = Violation(
                violationType = violationType,
                activityName = activityName ?: "",
                conformanceLevel = conformanceLevel,
                developerMessage = developerMessage,
                deviceId = deviceId
            )

            debugLog(Json.encodeToString(newViolation))
        }

        VisioAuxLogReport
            .getInstance()
            .clearAllReports()

        accessibilityLog("====================================\n\n\n\n\n\n")
    }

    private fun viewWasAlreadyExplored(view: View): Boolean {
        val wasExplored = exploredViews[view]
        return wasExplored == true
    }

    private fun markViewAsExplored(view: View) {
        val rootView = view.rootView
        exploredViews[rootView] = true
    }
}