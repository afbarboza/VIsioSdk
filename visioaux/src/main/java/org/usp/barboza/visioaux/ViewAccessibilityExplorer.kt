package org.usp.barboza.visioaux

import android.view.View
import com.google.android.apps.common.testing.accessibility.framework.integrations.visioaux.VisioAuxAccessibilityValidator
import com.google.android.apps.common.testing.accessibility.framework.integrations.visioaux.VisioAuxLogReport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.usp.barboza.visioaux.VisioAuxHelper.accessibilityLog
import org.usp.barboza.visioaux.VisioAuxHelper.debugLog
import org.usp.barboza.visioaux.https.ViolationRepository


object ViewAccessibilityExplorer {

    fun collectAccessibilityReport(view: View) {
        try {
            VisioAuxAccessibilityValidator()
                .setCaptureScreenshots(true)
                .setRunChecksFromRootView(true)
                .check(view)

            val checks = VisioAuxLogReport
                .getInstance()
                .reports

            val deviceId = VisioAuxViewListener.deviceId
            val activityName = VisioAuxViewListener.activityName
            val packageName = view.context.packageName

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

                reportViolation(packageName, newViolation)
            }

            VisioAuxLogReport
                .getInstance()
                .clearAllReports()

            accessibilityLog("====================================\n\n\n\n\n\n")
        } catch (e: Exception) {
            debugLog("VisioAux caught exception: ${e::class.simpleName}")
        }
    }

    private fun reportViolation(appId: String, violation: Violation) {
        CoroutineScope(Dispatchers.IO).launch {
            val repository = ViolationRepository()
            repository.reportViolation(appId, violation)
        }
    }
}