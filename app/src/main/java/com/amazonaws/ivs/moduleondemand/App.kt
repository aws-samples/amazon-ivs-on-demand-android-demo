package com.amazonaws.ivs.moduleondemand

import android.content.Context
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.amazonaws.ivs.moduleondemand.common.LineNumberDebugTree
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitcompat.SplitCompatApplication
import timber.log.Timber

class App : SplitCompatApplication(), ViewModelStoreOwner {

    override fun getViewModelStore() = appViewModelStore

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(LineNumberDebugTree("ModuleOnDemand"))
    }

    companion object {
        private val appViewModelStore: ViewModelStore by lazy { ViewModelStore() }
    }
}
