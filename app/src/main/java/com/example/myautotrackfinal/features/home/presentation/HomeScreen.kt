package com.example.myautotrackfinal.features.home.presentation

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myautotrackfinal.core.navigation.Screens
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
    val context = LocalContext.current

    var currentTime by remember { mutableStateOf("") }
    var totalServices by remember { mutableStateOf(0) }
    var nearbyServices by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            val calendar = java.util.Calendar.getInstance()
            val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
            val minute = calendar.get(java.util.Calendar.MINUTE)
            currentTime = String.format("%02d:%02d", hour, minute)
            totalServices = (15..25).random()
            nearbyServices = (8..12).random()
            delay(60000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFAFAFA), Color(0xFFF5F5F5))
                )
            )
            .verticalScroll(rememberScrollState())
    ) {

        RedModernHeader(
            currentTime = currentTime,
            onLogoutClick = {
                homeViewModel.logout()
                navController.navigate(Screens.Login.route) {
                    popUpTo(Screens.Home.route) { inclusive = true }
                }
                Toast.makeText(context, "👋 Sesión cerrada", Toast.LENGTH_SHORT).show()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🎭 EMOJI ANIMADO - Se mueve suavemente
        RedAnimatedGreetingCard()

        Spacer(modifier = Modifier.height(20.dp))

        // Estadísticas con números dinámicos
        RedQuickStatsRow(totalServices = totalServices, nearbyServices = nearbyServices)

        Spacer(modifier = Modifier.height(20.dp))

        // Balance card con gradiente rojo
        RedEnhancedBalanceCard()

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "🚗 Servicios Disponibles",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            // 🎭 BOTONES ANIMADOS - Aparecen uno por uno con delay
            RedAnimatedServiceCard(
                icon = Icons.Default.Add,
                text = "Agregar servicio",
                iconColor = Color(0xFFDC2626),
                delay = 0, // ← Sin delay, aparece primero
                onClick = {
                    navController.navigate(Screens.AddService.route)
                    Toast.makeText(context, "➕ Agregar nuevo servicio", Toast.LENGTH_SHORT).show()
                }
            )

            RedAnimatedServiceCard(
                icon = Icons.Default.List,
                text = "Ver servicios disponibles",
                iconColor = Color(0xFF991B1B),
                delay = 100, // ← 100ms después del anterior
                onClick = {
                    navController.navigate(Screens.ViewServicesWithMode.viewOnly())
                    Toast.makeText(context, "👁️ Solo visualización", Toast.LENGTH_SHORT).show()
                }
            )

            RedAnimatedServiceCard(
                icon = Icons.Default.Edit,
                text = "Modificar servicio",
                iconColor = Color(0xFFB91C1C),
                delay = 200, // ← 200ms después del primero
                onClick = {
                    navController.navigate(Screens.ViewServicesWithMode.editOnly())
                    Toast.makeText(context, "✏️ Selecciona un servicio para modificarlo", Toast.LENGTH_SHORT).show()
                }
            )

            RedAnimatedServiceCard(
                icon = Icons.Default.Delete,
                text = "Eliminar servicio",
                iconColor = Color(0xFF7F1D1D),
                delay = 300, // ← 300ms después del primero
                onClick = {
                    navController.navigate(Screens.ViewServicesWithMode.deleteOnly())
                    Toast.makeText(context, "🗑️ Selecciona un servicio para eliminarlo", Toast.LENGTH_SHORT).show()
                }
            )

            // Botón destacado para servicios cercanos
            RedFeaturedServiceCard(
                icon = Icons.Default.LocationOn,
                text = "Ver servicios cercanos",
                subtitle = "Encuentra talleres cerca de ti",
                onClick = {
                    navController.navigate(Screens.NearbyServices.route)
                    Toast.makeText(context, "📍 Buscando servicios cercanos...", Toast.LENGTH_SHORT).show()
                }
            )
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun RedModernHeader(currentTime: String, onLogoutClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(8.dp),
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFFDC2626), Color(0xFF991B1B))
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("¡Hola! 👋", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                    Text("AUTOTRACK", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                    if (currentTime.isNotEmpty()) {
                        Text("🕐 $currentTime", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    }
                }

                IconButton(
                    onClick = onLogoutClick,
                    modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión", tint = Color.White)
                }
            }
        }
    }
}

// 🎭 EMOJI QUE SE MUEVE - Animación infinita que cambia el tamaño del emoji
@Composable
fun RedAnimatedGreetingCard() {
    // ✨ ANIMACIÓN INFINITA - El emoji crece y decrece suavemente
    val infiniteTransition = rememberInfiniteTransition(label = "greeting")
    val animatedFloat by infiniteTransition.animateFloat(
        initialValue = 0f,        // ← Valor inicial
        targetValue = 1f,         // ← Valor final
        animationSpec = infiniteRepeatable(
            animation = tween(3000), // ← 3 segundos de duración
            repeatMode = RepeatMode.Reverse // ← Va y viene (0→1→0→1...)
        ), label = "float"
    )

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDC2626).copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🚗 EMOJI ANIMADO - El tamaño cambia de 24sp a 28sp suavemente
            Text(
                text = "🚗",
                fontSize = (24 + animatedFloat * 4).sp // ← 24sp + (0-1) * 4sp = 24sp-28sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("¡Bienvenido de vuelta!", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C3E50))
                Text("Gestiona tus servicios automotrices fácilmente", fontSize = 12.sp, color = Color(0xFF7F8C8D))
            }
        }
    }
}

@Composable
fun RedQuickStatsRow(totalServices: Int, nearbyServices: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFDC2626)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("$totalServices", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Servicios\nTotales", color = Color.White.copy(alpha = 0.9f), fontSize = 10.sp, textAlign = TextAlign.Center)
            }
        }

        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF991B1B)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("$nearbyServices", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Cerca\nde Ti", color = Color.White.copy(alpha = 0.9f), fontSize = 10.sp, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun RedEnhancedBalanceCard() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(colors = listOf(Color(0xFFDC2626), Color(0xFF991B1B)))
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("💳 Saldo Total", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("$149,868", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("📈 +49.89%", color = Color(0xFFFFE5E5), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🚗", fontSize = 32.sp)
                }
            }
        }
    }
}

// 🎭 BOTONES ANIMADOS - Aparecen deslizándose desde la derecha uno por uno
@Composable
fun RedAnimatedServiceCard(
    icon: ImageVector,
    text: String,
    iconColor: Color,
    delay: Int,
    onClick: () -> Unit
) {

    var isVisible by remember { mutableStateOf(false) }

    // 🚀 TRIGGER DE ANIMACIÓN - Se ejecuta cuando se crea el componente
    LaunchedEffect(Unit) {
        delay(delay.toLong()) // ← Espera X milisegundos
        isVisible = true      // ← Hace visible el botón (activa la animación)
    }

    // ✨ ANIMACIÓN DE ENTRADA - Deslizamiento + desvanecimiento
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            initialOffsetX = { 300 }, // ← Comienza 300px a la derecha
            animationSpec = tween(500) // ← Dura 500ms
        ) + fadeIn(animationSpec = tween(500)) // ← También se desvanece gradualmente
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(iconColor.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(28.dp))
                }

                Spacer(modifier = Modifier.width(16.dp))
                Text(text, color = Color(0xFF2C3E50), fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowForward, contentDescription = "Ir", tint = iconColor, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun RedFeaturedServiceCard(icon: ImageVector, text: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(colors = listOf(Color(0xFFDC2626), Color(0xFFB91C1C)))
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(64.dp).background(Color.White.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(subtitle, color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                }

                Icon(Icons.Default.ArrowForward, contentDescription = "Ir", tint = Color.White, modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}