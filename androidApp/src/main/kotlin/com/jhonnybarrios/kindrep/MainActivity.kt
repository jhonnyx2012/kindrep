package com.jhonnybarrios.kindrep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jhonnybarrios.kindrep.platform.AndroidActivityHolder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        AndroidActivityHolder.currentActivity = this

        setContent {
            KindrepApp()
        }
    }

    override fun onResume() {
        super.onResume()
        AndroidActivityHolder.currentActivity = this
    }

    override fun onDestroy() {
        if (AndroidActivityHolder.currentActivity === this) {
            AndroidActivityHolder.currentActivity = null
        }
        super.onDestroy()
    }
}
