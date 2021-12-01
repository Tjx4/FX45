package com.platform45.fx45.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.platform45.fx45.MainActivity
import xyz.appic.common.extensions.FADE_IN_ACTIVITY
import xyz.appic.common.extensions.navigateToActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToActivity(MainActivity::class.java, null, FADE_IN_ACTIVITY)
        finish()
    }
}