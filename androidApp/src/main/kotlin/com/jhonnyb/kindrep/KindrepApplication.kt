package com.jhonnyb.kindrep

import android.app.Application
import com.jhonnyb.kindrep.dependencyinjection.initializeDependencyInjection
import org.koin.android.ext.koin.androidContext

class KindrepApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeDependencyInjection {
            androidContext(this@KindrepApplication)
        }
    }
}
