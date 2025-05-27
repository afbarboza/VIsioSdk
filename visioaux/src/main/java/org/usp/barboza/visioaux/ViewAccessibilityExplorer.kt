package org.usp.barboza.visioaux

import android.view.View
import com.google.android.apps.common.testing.accessibility.framework.integrations.visioaux.VisioAuxAccessibilityValidator
import com.google.android.apps.common.testing.accessibility.framework.integrations.visioaux.VisioAuxLogReport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.usp.barboza.visioaux.VisioAuxHelper.accessibilityLog
import org.usp.barboza.visioaux.VisioAuxHelper.debugLog
import java.util.Hashtable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.usp.barboza.visioaux.https.ViolationRepository


object ViewAccessibilityExplorer {

    fun collectAccessibilityReport(view: View) {
        VisioAuxAccessibilityValidator()
            .setCaptureScreenshots(true)
            .setRunChecksFromRootView(true)
            .check(view)

        val checks = VisioAuxLogReport
            .getInstance()
            .reports

        val deviceId = VisioAuxViewListener.deviceId
        val activityName = VisioAuxViewListener.activityName

        accessibilityLog("Found ${checks.size} errors or warnings in the app for the current view")
        for (check in checks) {
            debugLog(">>> (${deviceId ?: "null"}) $activityName: ${check.check.category}: ${check.violationLogMessage} ")

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

            reportViolation(newViolation)
        }

        VisioAuxLogReport
            .getInstance()
            .clearAllReports()

        accessibilityLog("====================================\n\n\n\n\n\n")
    }

    private fun reportViolation(violation: Violation) {
        CoroutineScope(Dispatchers.IO).launch {
            val repository = ViolationRepository()
            repository.reportViolation(violation)
        }
    }
}