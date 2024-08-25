package org.usp.barboza.visioaux

import android.view.View
import com.google.android.apps.common.testing.accessibility.framework.integrations.visioaux.VisioAuxAccessibilityValidator
import com.google.android.apps.common.testing.accessibility.framework.integrations.visioaux.VisioAuxLogReport
import org.usp.barboza.visioaux.VisioAuxHelper.accessibilityLog
import org.usp.barboza.visioaux.VisioAuxHelper.debugLog
import java.util.Hashtable

object ViewAccessibilityExplorer {
    private val exploredViews = Hashtable<View, Boolean>(200)

    fun collectAccessibilityReport(view: View) {
        /**
         * WARNING: THIS IS A REALLY BAD HEURISTIC TO AVOID STATE EXPLOSION. THIS SHALL BE IMPROVED
         */
        /* if (viewWasAlreadyExplored(view)) {
            return
        }
        markViewAsExplored(view) */

        if (view == null) {
            return
        }

        VisioAuxAccessibilityValidator()
            .check(view)

        val logs  = VisioAuxLogReport
            .getInstance()
            .logMessage

        for (log in logs) {
            accessibilityLog(log)
        }

        accessibilityLog("Found ${logs.size} errors or warnings in the app for the current view")

        val checks = VisioAuxLogReport
            .getInstance()
            .checks

        for (check in checks) {
            debugLog("Capture check of type $check")
        }

        accessibilityLog("====================================\n\n\n\n\n\n")
    }

    private fun viewWasAlreadyExplored(view: View): Boolean {
        val wasExplored = exploredViews[view]
        return wasExplored != null && wasExplored == true
    }

    private fun markViewAsExplored(view: View) {
        val rootView = view.rootView
        exploredViews[rootView] = true
    }
}