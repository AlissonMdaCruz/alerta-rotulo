package com.amdcruz.alertarotulo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.amdcruz.alertarotulo.core.ui.theme.AlertaRotuloTheme
import com.amdcruz.alertarotulo.navigation.AlertaRotuloNavDisplay
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlertaRotuloTheme {
                AlertaRotuloNavDisplay()
            }
        }
    }
}
