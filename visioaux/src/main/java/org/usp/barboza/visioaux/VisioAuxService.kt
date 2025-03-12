package org.usp.barboza.visioaux

import android.accessibilityservice.AccessibilityService
import android.os.Build
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import org.usp.barboza.visioaux.ViewAccessibilityExplorer.collectAccessibilityReport
import org.usp.barboza.visioaux.VisioAuxHelper.debugLog
import org.usp.barboza.visioaux.VisioAuxHelper.shouldIgnoreEvent
import java.util.Hashtable

class VisioAuxService : AccessibilityService() {

    private val exploredNodes = Hashtable<AccessibilityNodeInfo, Boolean>(200)


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val packageSource = event?.packageName ?: ""
        if (packageSource != applicationContext.packageName) {
            return
        }

        /* if (shouldIgnoreEvent(event)) {
            debugLog("Accessibility event should be ignored. Silently give up")
            return
        } */

        val root = event?.source
        exploreAccessibilityNode(root)
    }

    override fun onInterrupt() {
    }

    private fun exploreAccessibilityNode(node: AccessibilityNodeInfo?) {
        if (node == null) {
            /* Leaf node was already reached at this point, stop probing for accessibility */
            return
        }

        /* if (nodeWasAlreadyExplored(node)) {
            /* The accessibility of this node was already checked, silently give up */
            debugLog(">>> ANI node marked as explored. Returning...")
            return
        }

        /* For further reference, the node must be marked as explored */
        markNodeAsExplored(node) */

        /* Actually check the accessibility */
        probeAccessibility(node)

        /* Recursively explores the subtree */
        val nodeDegree = node.childCount
        for (i in 0..(nodeDegree - 1)) {
            exploreAccessibilityNode(node.getChild(i))
        }
    }

    private fun nodeWasAlreadyExplored(node: AccessibilityNodeInfo): Boolean {
        val wasExplored = exploredNodes[node]
        return wasExplored ?: false
    }

    private fun markNodeAsExplored(node: AccessibilityNodeInfo) {
        exploredNodes[node] = true
    }

    private fun probeAccessibility(node: AccessibilityNodeInfo) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!node.isImportantForAccessibility) {
                return
            }
        }

        showNodeDetails(node)
        val view = getViewDetails(node)

        if (view != null) {
            collectAccessibilityReport(view)
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