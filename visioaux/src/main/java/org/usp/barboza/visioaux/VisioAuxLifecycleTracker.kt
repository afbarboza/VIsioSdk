package org.usp.barboza.visioaux

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import java.util.UUID

class VisioAuxLifecycleTracker : ActivityLifecycleCallbacks {

    private lateinit var deviceId: String

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        deviceId = getDeviceId(activity) ?: createDeviceId(activity)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        val rootView = activity.window.decorView.rootView
        VisioAuxViewListener
            .registerForAccessibilityEvents(rootView, activity.javaClass.name, deviceId)
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

    private fun getDeviceId(context: Context): String? {
        val sharedPrefs = context.getSharedPreferences(
            "DEVICE_RECORD", MODE_PRIVATE
        )

        val deviceId = sharedPrefs.getString("DEVICE_ID", null)
        return deviceId
    }

    private fun createDeviceId(context: Context): String {
        val editor = context
            .getSharedPreferences("DEVICE_RECORD", MODE_PRIVATE)
            .edit()

        val deviceId = generatedeviceId()
        editor.putString("DEVICE_ID", deviceId)
        editor.apply()

        return deviceId
    }

    private fun generatedeviceId(): String {
        return "${UUID.randomUUID()}${System.currentTimeMillis()}"
    }
}