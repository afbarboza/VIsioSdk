package org.usp.barboza.visioaux

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.usp.barboza.visioaux.VisioAuxHelper.accessibilityLog
import org.usp.barboza.visioaux.VisioAuxHelper.debugLog
import java.util.UUID

class VisioAuxLifecycleTracker : ActivityLifecycleCallbacks {

    private lateinit var deviceId: String
    private lateinit var rootView: View
    private lateinit var accessibilityEvaluationScheduler: Job

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        deviceId = getDeviceId(activity) ?: createDeviceId(activity)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        rootView = activity.window.decorView.rootView
        if (VisioAuxServiceChecker.isVisioAuxRuntimePermissionGranted(activity)) {
            initAccessibilityEvaluationScheduler(activity)
            accessibilityEvaluationScheduler.start()
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (VisioAuxServiceChecker.isVisioAuxRuntimePermissionGranted(activity)) {
            accessibilityEvaluationScheduler.cancel()
            CoroutineScope(Dispatchers.IO).launch {
                accessibilityEvaluationScheduler.join()
            }
        }
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

    private fun initAccessibilityEvaluationScheduler(currentActivity: Activity) {
        accessibilityEvaluationScheduler = CoroutineScope(Dispatchers.IO)
            .launch(
                start = CoroutineStart.LAZY
            ) {
            while (true) {
                rootView = currentActivity.window.decorView.rootView
                if (rootView == null) {
                    debugLog("Uncapable of record root view as a valid view for lifecyle tracker!")
                    continue
                } else {
                    VisioAuxViewListener
                        .registerForAccessibilityEvents(rootView, currentActivity.javaClass.name, deviceId)

                    ViewAccessibilityExplorer
                        .collectAccessibilityReport(rootView)
                }

                val allRoots = getAllRootViews()
                for (currentRoot in allRoots) {
                    try {
                        ViewAccessibilityExplorer
                            .collectAccessibilityReport(currentRoot)
                    } catch (e: Exception) {
                        debugLog("Error while trying to probe for View")
                    }
                }

                delay(3000)
            }
        }

        accessibilityEvaluationScheduler.invokeOnCompletion {
            VisioAuxViewListener
                .unregisterForAccessibilityEvents()
        }
    }


    fun getAllRootViews(): List<View> {
        val rootViews = mutableListOf<View>()

        try {
            val wmGlobalClass = Class.forName("android.view.WindowManagerGlobal")
            val getInstanceMethod = wmGlobalClass.getMethod("getInstance")
            val wmGlobalInstance = getInstanceMethod.invoke(null)

            // Get view names (needed to access root views in Android 11+)
            val getViewRootNamesMethod = wmGlobalClass.getMethod("getViewRootNames")
            val viewRootNames = getViewRootNamesMethod.invoke(wmGlobalInstance) as Array<String>

            val getRootViewMethod = wmGlobalClass.getMethod("getRootView", String::class.java)

            for (name in viewRootNames) {
                val rootView = getRootViewMethod.invoke(wmGlobalInstance, name) as View
                rootViews.add(rootView)
            }

        } catch (e: Exception) {
            debugLog("Failed to get root views")
        }

        return rootViews
    }
}