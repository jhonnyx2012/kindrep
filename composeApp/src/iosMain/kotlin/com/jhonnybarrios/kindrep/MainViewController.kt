package com.jhonnybarrios.kindrep

import androidx.compose.ui.window.ComposeUIViewController
import com.jhonnybarrios.kindrep.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController {
        KindrepApp()
    }
}
