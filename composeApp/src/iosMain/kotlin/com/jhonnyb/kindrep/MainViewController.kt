package com.jhonnyb.kindrep

import androidx.compose.ui.window.ComposeUIViewController
import com.jhonnyb.kindrep.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController {
        KindrepApp()
    }
}
