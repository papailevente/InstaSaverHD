package com.example.instasaverhd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.instasaverhd.ui.main.MainScreen
import com.example.instasaverhd.ui.theme.InstaSaverHDTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InstaSaverHDTheme {
                MainScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
