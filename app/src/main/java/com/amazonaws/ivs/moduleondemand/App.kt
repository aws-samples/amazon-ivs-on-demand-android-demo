package com.amazonaws.ivs.moduleondemand

import android.content.Context
import com.amazonaws.ivs.moduleondemand.common.LineNumberDebugTree
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitcompat.SplitCompatApplication
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : SplitCompatApplication() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(LineNumberDebugTree())
    }
}
