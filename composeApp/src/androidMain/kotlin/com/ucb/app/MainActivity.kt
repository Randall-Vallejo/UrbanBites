package com.ucb.app

import urbanbites.com.App as UrbanBitesApp
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

//import com.ucb.app.core.data.worker.MyScheduler

class MainActivity : ComponentActivity() {
    
    private var destination by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Iniciamos el Scheduler de tareas en segundo plano
       // MyScheduler(this).start()
        // Opcional: prueba rápida inmediata
       // MyScheduler(this).runNow()

        destination = intent.getStringExtra("destination")

        setContent {
            UrbanBitesApp()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        destination = intent.getStringExtra("destination")
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    UrbanBitesApp()
}
