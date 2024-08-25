package org.usp.barboza.visioaux

import android.view.View
import com.google.android.apps.common.testing.accessibility.framework.integrations.visioaux.VisioAuxLogReport
import org.usp.barboza.visioaux.ViewAccessibilityExplorer.collectAccessibilityReport
import org.usp.barboza.visioaux.VisioAuxHelper.accessibilityLog
import java.lang.ref.WeakReference

object VisioAuxViewListener {
    private var wrRootView: WeakReference<View>? = null

    fun registerForAccessibilityEvents(rootView: View, screenName: String) {
        wrRootView = WeakReference(rootView)
        accessibilityLog("=================***=================")
        accessibilityLog("Evaluating screen $screenName")
        accessibilityLog("=================***=================")

        collectAccessibilityReport(rootView)
    }

    fun unregisterForAccessibilityEvents() {
        wrRootView = null

        VisioAuxLogReport
            .getInstance()
            .clearLogs()
    }

    fun getViewFromAccessibilityEvent(viewId: Int): View? {
        return wrRootView?.get()?.rootView?.findViewById(viewId)
    }
}