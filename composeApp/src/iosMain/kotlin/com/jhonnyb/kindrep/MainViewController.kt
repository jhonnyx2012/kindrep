package com.jhonnyb.kindrep

import androidx.compose.ui.window.ComposeUIViewController
import com.jhonnyb.kindrep.dependencyinjection.initializeDependencyInjection
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initializeDependencyInjection()
    return ComposeUIViewController {
        KindrepRoot()
    }
}
