package com.samkt.vercel

import android.os.Bundle
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.samkt.hela.theme.HelaTheme
import com.samkt.vercel.ui.gallery.HelaGallery

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemDark = isSystemInDarkTheme()
            var dark by rememberSaveable { mutableStateOf(systemDark) }
            // Keep status/nav bar icons readable when the in-app theme differs from the system's.
            DisposableEffect(dark) {
                val style = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { dark }
                enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
                onDispose {}
            }
            HelaTheme(darkTheme = dark) {
                HelaGallery(darkTheme = dark, onDarkThemeChange = { dark = it })
            }
        }
    }
}
