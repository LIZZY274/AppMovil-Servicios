package com.example.myautotrackfinal.features.service.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SyncStatus(
    val isConnected: Boolean,
    val isSyncing: Boolean,
    val pendingCount: Int,
    val lastSyncMessage: String? = null,
    val hasError: Boolean = false
)

@Composable
fun SyncStatusComponent(
    syncStatus: SyncStatus,
    onSyncClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardColor = when {
        !syncStatus.isConnected -> Color(0xFFDC2626) // Rojo - Sin conexión
        syncStatus.hasError -> Color(0xFFEA580C) // Naranja - Error
        syncStatus.isSyncing -> Color(0xFF2563EB) // Azul - Sincronizando
        syncStatus.pendingCount > 0 -> Color(0xFFCA8A04) // Amarillo - Pendientes
        else -> Color(0xFF16A34A) // Verde - Todo bien
    }

    val textColor = Color.White
    val icon = when {
        !syncStatus.isConnected -> Icons.Default.CloudOff
        syncStatus.hasError -> Icons.Default.ErrorOutline
        syncStatus.isSyncing -> Icons.Default.Sync
        syncStatus.pendingCount > 0 -> Icons.Default.Schedule
        else -> Icons.Default.CloudDone
    }

    val statusText = when {
        !syncStatus.isConnected -> "Sin conexión"
        syncStatus.hasError -> "Error en sincronización"
        syncStatus.isSyncing -> "Sincronizando..."
        syncStatus.pendingCount > 0 -> "${syncStatus.pendingCount} pendientes"
        else -> "Todo sincronizado"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSyncClick() },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Icono animado
                if (syncStatus.isSyncing) {
                    val rotation by rememberInfiniteTransition(label = "rotation").animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "sync_rotation"
                    )

                    Icon(
                        icon,
                        contentDescription = statusText,
                        tint = textColor,
                        modifier = Modifier
                            .size(24.dp)
                            .graphicsLayer { rotationZ = rotation }
                    )
                } else {
                    Icon(
                        icon,
                        contentDescription = statusText,
                        tint = textColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = statusText,
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    syncStatus.lastSyncMessage?.let { message ->
                        Text(
                            text = message,
                            color = textColor.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                }
            }

            // Botón de acción
            if (!syncStatus.isSyncing && syncStatus.isConnected) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Sincronizar ahora",
                    tint = textColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun CompactSyncIndicator(
    syncStatus: SyncStatus,
    modifier: Modifier = Modifier
) {
    if (syncStatus.pendingCount > 0 || syncStatus.isSyncing) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (syncStatus.isSyncing) {
                val rotation by rememberInfiniteTransition(label = "compact_rotation").animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "compact_sync_rotation"
                )

                Icon(
                    Icons.Default.Sync,
                    contentDescription = "Sincronizando",
                    tint = Color(0xFF2563EB),
                    modifier = Modifier
                        .size(16.dp)
                        .graphicsLayer { rotationZ = rotation }
                )
            } else {
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = "Pendientes",
                    tint = Color(0xFFCA8A04),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = if (syncStatus.isSyncing) "Sync..." else "${syncStatus.pendingCount}",
                color = if (syncStatus.isSyncing) Color(0xFF2563EB) else Color(0xFFCA8A04),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}