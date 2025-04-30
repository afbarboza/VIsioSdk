package org.usp.barboza.visioaux

import android.content.Context
import android.devicelock.DeviceId
import android.telephony.TelephonyManager
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.apps.common.testing.accessibility.framework.integrations.visioaux.VisioAuxLogReport
import org.usp.barboza.visioaux.ViewAccessibilityExplorer.collectAccessibilityReport
import org.usp.barboza.visioaux.VisioAuxHelper.accessibilityLog
import java.lang.ref.WeakReference

object VisioAuxViewListener {
    private var wrRootView: WeakReference<View>? = null
    var activityName: String? = null
    var deviceId: String? = null

    fun registerForAccessibilityEvents(rootView: View, screenName: String, deviceId: String) {
        wrRootView = WeakReference(rootView)
        accessibilityLog("=================***=================")
        accessibilityLog("Evaluating screen $screenName")
        accessibilityLog("=================***=================")

        activityName = screenName
        this.deviceId = deviceId
    }

    fun unregisterForAccessibilityEvents() {
        VisioAuxLogReport
            .getInstance()
            .clearAllReports()
    }

    fun getViewFromAccessibilityEvent(viewId: Int): View? {
        return wrRootView?.get()?.rootView?.findViewById(viewId)
    }

    fun getRootviewAsFallback(): View? {
        return wrRootView?.get()?.rootView
    }
}