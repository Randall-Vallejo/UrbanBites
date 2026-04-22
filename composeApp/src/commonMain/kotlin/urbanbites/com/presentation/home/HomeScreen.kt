package urbanbites.com.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import urbanbites.com.Res
import urbanbites.com.burger


val DarkBackground = Color(0xFF161616)
val SearchBarColor = Color(0xFF2C2C2C)
val OrangeCategory = Color(0xFFFF7A00)
val TextGray = Color(0xFFAAAAAA)

@Composable
fun HomeScreen() {
    Scaffold(
        bottomBar = { BottomNavBar() },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            TopLocationBar()
            Spacer(modifier = Modifier.height(24.dp))
            SearchBarPlaceholder()
            Spacer(modifier = Modifier.height(24.dp))
            CategoriesRow()
            Spacer(modifier = Modifier.height(24.dp))
            RestaurantCard()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TopLocationBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Ubicacion Actual",
                color = TextGray,
                fontSize = 12.sp
            )
            Text(
                text = "Cochabamba 📍",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Icono de perfil
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Perfil",
                tint = Color.White
            )
        }
    }
}

@Composable
fun SearchBarPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SearchBarColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Que se te antoja hoy?",
            color = TextGray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun CategoriesRow() {
    val categories = listOf("🍔", "🍕", "🌮", "🍦")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        categories.forEach { emoji ->
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(OrangeCategory),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 28.sp)
            }
        }
    }
}

@Composable
fun RestaurantCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SearchBarColor)
    ) {
        Column {
            // Placeholder para la imagen de la hamburguesa
            Image(
                painter = painterResource(Res.drawable.burger), // El nombre de tu foto
                contentDescription = "Hamburguesa del Valle",
                contentScale = androidx.compose.ui.layout.ContentScale.Crop, // Esto hace que la foto llene el espacio sin deformarse
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Hamburguesas del Valle",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⭐ 4.8  |  🕒 20-30 min  |  🛵 Gratis",
                    color = TextGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun BottomNavBar() {
    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = TextGray,
                indicatorColor = Color.Transparent
            )
        )
        // Usamos Iconos de Material como placeholders de los que tienes en tu mockup
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = "Favoritos") }, // Cambiar a Corazón luego
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Recibos") },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            selected = false,
            onClick = { }
        )
    }
}
