package com.ph.nasaimagesearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ph.nasaimagesearch.ui.navigation.NasaImageSearchNavHost
import com.ph.nasaimagesearch.ui.theme.NasaImageSearchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NasaImageSearchTheme {
                setupSurface()
            }
        }
    }
}

@Composable
private fun setupSurface() {
    // A surface container using the 'background' color from the theme
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        NasaImageSearchNavHost(modifier = Modifier.fillMaxSize())
    }
}