package org.usp.barboza.visioaux

import android.accessibilityservice.AccessibilityService
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import org.usp.barboza.visioaux.ViewAccessibilityExplorer.collectAccessibilityReport
import org.usp.barboza.visioaux.VisioAuxHelper.debugLog

class VisioAuxService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val packageSource = event?.packageName ?: ""
        if (packageSource != applicationContext.packageName) {
            return
        }

        val root = event?.source
        root?.refresh()
        exploreAccessibilityNode(root)
    }

    override fun onInterrupt() {
    }

    private fun exploreAccessibilityNode(node: AccessibilityNodeInfo?) {
        if (node == null) {
            /* Leaf node was already reached at this point, stop probing for accessibility */
            return
        }

        /* Actually check the accessibility */
        probeAccessibility(node)
    }

    private fun probeAccessibility(node: AccessibilityNodeInfo) {

        showNodeDetails(node)
        var view = getViewDetails(node)

        if (view != null) {
            collectAccessibilityReport(view)
        } else {
            view = VisioAuxViewListener.getRootviewAsFallback()
            if (view == null) {
                debugLog("Uncapable of getting root view. Halting accessibility report")
            } else {
                collectAccessibilityReport(view)
            }
        }
    }

    private fun showNodeDetails(node: AccessibilityNodeInfo) {
        debugLog("Package: " + node.packageName)

        val str = StringBuilder()
        str.append(node.viewIdResourceName)
            .append("(")
            .append(node.className)
            .append(")")

        debugLog("Widget Details: $str")
    }

    private fun getViewDetails(node: AccessibilityNodeInfo): View? {
        val viewResourceName = node.viewIdResourceName
        val resources = resources

        if (resources == null) {
            debugLog("Unable to capture Resources instance for this applications package")
            return null
        }

        if (viewResourceName == null) {
            debugLog("Given resource name is null")
            return null
        }

        val viewId = resources.getIdentifier(viewResourceName, null, null)

        if (viewId == 0) {
            debugLog("Unable to capture current view id")
            return null
        }

        debugLog("Captured View Id is: $viewId")

        val view = VisioAuxViewListener
            .getViewFromAccessibilityEvent(viewId)

        if (view == null) {
            debugLog("Unable to map view from given id")
            return null
        }

        return view
    }
}