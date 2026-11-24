package com.jhonnybarrios.kindrep

import android.app.Application
import com.jhonnybarrios.kindrep.di.initKoin
import org.koin.android.ext.koin.androidContext

class KindrepApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@KindrepApplication)
        }
    }
}
