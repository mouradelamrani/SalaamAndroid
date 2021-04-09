package com.mouradelamrani.salaam

import android.app.Application
import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics

class SalaamApp : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.getInstance(base).wrapContext(base))
    }

    override fun onCreate() {
        super.onCreate()
        // Crashlytics
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

}