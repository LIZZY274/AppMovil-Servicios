package com.example.myautotrackfinal.features.nearby.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myautotrackfinal.features.nearby.domain.model.ServiceLocation
import kotlin.math.*
import kotlin.random.Random

@Composable
fun MapComponent(
    modifier: Modifier = Modifier,
    currentLocation: Pair<Double, Double>?,
    services: List<ServiceLocation>,
    onServiceClick: (ServiceLocation) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD),
                        Color(0xFFF1F8E9)
                    )
                )
            )
    ) {
        //
        SimpleMapBackground()


        services.forEachIndexed { index, service ->
            val position = calculateSimplePosition(index, services.size)
            BeautifulServiceMarker(
                service = service,
                position = position,
                onClick = { onServiceClick(service) }
            )
        }



        SimpleMapControls(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )

        SimpleInfoPanel(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            servicesCount = services.size
        )
    }
}


@Composable
fun SimpleMapBackground() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .offset(y = 200.dp)
                .background(
                    Color(0xFF9E9E9E),
                    RoundedCornerShape(4.dp)
                )
        )

        // Líneas de carretera
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .offset(y = 203.dp)
                .background(
                    Color.White,
                    RoundedCornerShape(1.dp)
                )
        )



        // Áreas verdes
        repeat(8) { i ->
            Box(
                modifier = Modifier
                    .size((20 + Random.nextInt(30)).dp)
                    .offset(
                        x = (Random.nextInt(300)).dp,
                        y = (Random.nextInt(250)).dp
                    )
                    .background(
                        Color(0xFF4CAF50).copy(alpha = 0.3f),
                        CircleShape
                    )
            )
        }
    }
}


@Composable
fun BeautifulServiceMarker(
    service: ServiceLocation,
    position: Pair<Float, Float>,
    onClick: () -> Unit
) {
    val markerColor = when (service.type) {
        "Taller Mecánico" -> Color(0xFFFF5722)
        "Llantera" -> Color(0xFF2196F3)
        "Hojalatería" -> Color(0xFFFF9800)
        "Electricidad Automotriz" -> Color(0xFF9C27B0)
        "Refacciones" -> Color(0xFF4CAF50)
        "Servicios de Emergencia" -> Color(0xFFF44336)
        else -> Color(0xFF607D8B)
    }

    val emoji = when (service.type) {
        "Taller Mecánico" -> "🔧"
        "Llantera" -> "🛞"
        "Hojalatería" -> "🎨"
        "Electricidad Automotriz" -> "⚡"
        "Refacciones" -> "🔩"
        "Servicios de Emergencia" -> "🚑"
        else -> "🏪"
    }

    Column(
        modifier = Modifier
            .offset(
                x = position.first.dp,
                y = position.second.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(45.dp)
                .shadow(6.dp, CircleShape)
                .background(markerColor, CircleShape)
                .border(3.dp, Color.White, CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 20.sp
            )
        }


        Spacer(modifier = Modifier.height(4.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.shadow(2.dp, RoundedCornerShape(6.dp))
        ) {
            Text(
                text = if (service.name.length > 10) service.name.take(10) + "..." else service.name,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                textAlign = TextAlign.Center
            )
        }


        service.distance?.let { distance ->
            Text(
                text = "${String.format("%.1f", distance)}km",
                color = markerColor,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}


@Composable
fun SimpleMapControls(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = { /* Zoom in */ },
                modifier = Modifier.size(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF5F5F5)
                ),
                contentPadding = PaddingValues(0.dp),
                shape = CircleShape
            ) {
                Text("+", fontSize = 18.sp, color = Color.Black, fontWeight = FontWeight.Bold)
            }


            Button(
                onClick = { /* Zoom out */ },
                modifier = Modifier.size(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF5F5F5)
                ),
                contentPadding = PaddingValues(0.dp),
                shape = CircleShape
            ) {
                Text("−", fontSize = 18.sp, color = Color.Black, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { /* Centrar */ },
                modifier = Modifier.size(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                ),
                contentPadding = PaddingValues(0.dp),
                shape = CircleShape
            ) {
                Icon(
                    Icons.Default.MyLocation,
                    contentDescription = "Mi ubicación",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun SimpleInfoPanel(
    modifier: Modifier = Modifier,
    servicesCount: Int
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$servicesCount",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )
            Text(
                text = "servicios\ncercanos",
                fontSize = 12.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .background(
                        Color(0xFF4CAF50),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "📍 Suchiapa",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private fun calculateSimplePosition(index: Int, total: Int): Pair<Float, Float> {
    val centerX = 200f
    val centerY = 200f
    val radius = 60f + (index * 25f)
    val angle = (index * 2 * PI / max(total, 1)).toFloat()

    val x = centerX + radius * cos(angle)
    val y = centerY + radius * sin(angle)

    return Pair(
        x.coerceIn(30f, 320f),
        y.coerceIn(50f, 350f)
    )
}