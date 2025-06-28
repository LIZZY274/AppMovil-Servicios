package com.example.myautotrackfinal.features.service.presentation

import android.content.Context
import android.util.Log // <-- Importa Log para depuración
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myautotrackfinal.features.service.data.model.Service
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun ServiceItem(
    service: Service,
    onEditClick: (Service) -> Unit,
    onDeleteClick: (Service) -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Asegúrate de que service.fecha no sea nulo si tu modelo lo permite
    val formattedDate = try {
        if (service.fecha.isNullOrEmpty()) { // Añade esta comprobación si service.fecha puede ser nulo o vacío
            "Fecha Desconocida"
        } else {
            OffsetDateTime.parse(service.fecha)
                .format(dateFormatter)
        }
    } catch (e: DateTimeParseException) {
        Log.e("ServiceItem", "Error al parsear fecha: ${service.fecha}", e) // Loguea el error
        service.fecha // Mantener la fecha original si hay un error de parseo
    } catch (e: Exception) { // Captura cualquier otra excepción inesperada
        Log.e("ServiceItem", "Error inesperado al formatear fecha: ${service.fecha}", e)
        service.fecha
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = service.tipo, style = MaterialTheme.typography.titleMedium, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Fecha: $formattedDate", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Costo: $${String.format("%.2f", service.costo)}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Taller: ${service.taller}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Descripción: ${service.descripcion}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onEditClick(service) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(4.dp))
                    Text("Editar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = { onDeleteClick(service) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    Spacer(Modifier.width(4.dp))
                    Text("Eliminar", color = Color.Red)
                }
            }
        }
    }
}

fun showDeleteConfirmationDialog(context: Context, service: Service, onDeleteConfirmed: () -> Unit) {
    // Protección adicional para el contexto, aunque LocalContext.current suele ser seguro
    if (context == null) {
        Log.e("DeleteDialog", "Contexto es nulo, no se puede mostrar el diálogo de eliminación.")
        return
    }

    val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDateForDialog = try {
        if (service.fecha.isNullOrEmpty()) { // Protección para la fecha del diálogo
            "Fecha Desconocida"
        } else {
            OffsetDateTime.parse(service.fecha).format(dateFormatter)
        }
    } catch (e: DateTimeParseException) {
        Log.e("DeleteDialog", "Error al parsear fecha para diálogo: ${service.fecha}", e)
        service.fecha
    } catch (e: Exception) {
        Log.e("DeleteDialog", "Error inesperado al formatear fecha para diálogo: ${service.fecha}", e)
        service.fecha
    }

    materialAlertDialogBuilder.setTitle("Confirmar eliminación")
    materialAlertDialogBuilder.setMessage("¿Estás seguro de que quieres eliminar el servicio de ${service.tipo} del $formattedDateForDialog?")
    materialAlertDialogBuilder.setNegativeButton("Cancelar") { dialog, _ ->
        dialog.dismiss()
    }
    materialAlertDialogBuilder.setPositiveButton("Eliminar") { dialog, _ ->
        try { // Añade un try-catch aquí para la llamada de confirmación
            onDeleteConfirmed()
        } catch (e: Exception) {
            Log.e("DeleteDialog", "Error al ejecutar onDeleteConfirmed: ${e.message}", e)
            // Opcional: mostrar un Toast aquí si el error es crítico para el usuario
            // Toast.makeText(context, "Error interno al eliminar.", Toast.LENGTH_SHORT).show()
        }
        dialog.dismiss()
    }
    try {
        materialAlertDialogBuilder.show()
    } catch (e: Exception) {
        Log.e("DeleteDialog", "Error al mostrar el diálogo: ${e.message}", e)
        // Opcional: Toast si el diálogo no se puede mostrar
        // Toast.makeText(context, "No se pudo mostrar el diálogo de eliminación.", Toast.LENGTH_SHORT).show()
    }
}