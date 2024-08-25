package org.usp.barboza.visioaux

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

class VisioAuxLifecycleTracker : ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        val rootView = activity.window.decorView.rootView
        VisioAuxViewListener
            .registerForAccessibilityEvents(rootView, activity.javaClass.name)
    }

    override fun onActivityPaused(activity: Activity) {
        VisioAuxViewListener
            .unregisterForAccessibilityEvents()
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}