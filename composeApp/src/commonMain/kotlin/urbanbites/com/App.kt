package urbanbites.com

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import urbanbites.com.presentation.navigation.AppNavHost

@Composable
fun App() {
    MaterialTheme {
        AppNavHost()
    }
}
