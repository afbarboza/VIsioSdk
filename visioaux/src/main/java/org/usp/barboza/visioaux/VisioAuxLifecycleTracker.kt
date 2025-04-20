package org.usp.barboza.visioaux

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.usp.barboza.visioaux.VisioAuxHelper.accessibilityLog
import java.util.UUID

class VisioAuxLifecycleTracker : ActivityLifecycleCallbacks {

    private lateinit var deviceId: String
    private lateinit var accessibilityEvaluationScheduler: Job

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        deviceId = getDeviceId(activity) ?: createDeviceId(activity)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        initAccessibilityEvaluationScheduler(activity)
        accessibilityEvaluationScheduler.start()
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        accessibilityEvaluationScheduler.cancel()

        CoroutineScope(Dispatchers.IO).launch {
            accessibilityEvaluationScheduler.join()
        }
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

    private fun initAccessibilityEvaluationScheduler(currentActivity: Activity) {
        accessibilityEvaluationScheduler = CoroutineScope(Dispatchers.Main)
            .launch(
                start = CoroutineStart.LAZY
            ) {
            while (true) {
                val rootView = currentActivity.window.decorView.rootView
                if (rootView == null) {
                    throw RuntimeException("Uncapable of RECORD root view as a valid view for lifecyle tracker!")
                } else {
                    accessibilityLog("Successfully recording current root view ")
                }

                VisioAuxViewListener
                    .registerForAccessibilityEvents(rootView, currentActivity.javaClass.name, deviceId)

                delay(5000)
            }
        }

        accessibilityEvaluationScheduler.invokeOnCompletion {
            accessibilityLog("Canceling coroutine...")

            VisioAuxViewListener
                .unregisterForAccessibilityEvents()
        }
    }
}