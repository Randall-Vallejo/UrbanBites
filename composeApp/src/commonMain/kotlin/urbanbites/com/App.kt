package urbanbites.com
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.ucb.app.demo.presentation.screen.DemoFuncionalidadesScreen

@Composable
fun App() {
    MaterialTheme {
        // Mostramos directamente la pantalla de demo para el examen
        DemoFuncionalidadesScreen()
    }
}
