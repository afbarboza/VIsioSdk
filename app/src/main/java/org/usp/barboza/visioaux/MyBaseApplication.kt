package org.usp.barboza.visioaux

import android.app.Application


class MyBaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        /* Register lifecycle callbacks */
        registerActivityLifecycleCallbacks(
            VisioAuxLifecycleTracker()
        )
    }
}