package com.jhonnyb.kindrep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jhonnyb.kindrep.platform.AndroidActivityRegistry

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        AndroidActivityRegistry.currentActivity = this

        setContent {
            KindrepRoot()
        }
    }

    override fun onResume() {
        super.onResume()
        AndroidActivityRegistry.currentActivity = this
    }

    override fun onDestroy() {
        if (AndroidActivityRegistry.currentActivity === this) {
            AndroidActivityRegistry.currentActivity = null
        }
        super.onDestroy()
    }
}
