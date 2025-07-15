package com.example.myautotrackfinal.features.service.presentation

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


object UpdateRedTheme {
    val Primary = Color(0xFFB91C1C)
    val Light = Color(0xFFFEF2F2)
    val Medium = Color(0xFFFECACA)
    val Dark = Color(0xFF991B1B)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateServiceScreen(navController: NavController, serviceId: String, updateServiceViewModel: UpdateServiceViewModel = viewModel()) {
    val context = LocalContext.current

    var tipo by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var costo by remember { mutableStateOf("") }
    var taller by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    var originalTipo by remember { mutableStateOf("") }
    var originalFecha by remember { mutableStateOf("") }
    var originalCosto by remember { mutableStateOf("") }
    var originalTaller by remember { mutableStateOf("") }
    var originalDescripcion by remember { mutableStateOf("") }

    val serviceDetails by updateServiceViewModel.service.observeAsState()
    val updateServiceSuccess by updateServiceViewModel.updateSuccess.observeAsState()
    val errorMessage by updateServiceViewModel.errorMessage.observeAsState()
    val isLoading by updateServiceViewModel.isLoading.observeAsState(false)


    LaunchedEffect(serviceId) {
        updateServiceViewModel.loadService(serviceId)
    }


    LaunchedEffect(serviceDetails) {
        serviceDetails?.let { service ->
            tipo = service.tipo
            fecha = service.fecha
            costo = service.costo.toString()
            taller = service.taller
            descripcion = service.descripcion ?: ""


            originalTipo = service.tipo
            originalFecha = service.fecha
            originalCosto = service.costo.toString()
            originalTaller = service.taller
            originalDescripcion = service.descripcion ?: ""
        }
    }


    LaunchedEffect(updateServiceSuccess) {
        if (updateServiceSuccess == true) {
            Toast.makeText(context, "✅ Servicio actualizado exitosamente", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
            updateServiceViewModel.clearMessages()
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            updateServiceViewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "✏️ Modificar Servicio",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = UpdateRedTheme.Primary)
            )
        },
        modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAFC))
    ) { paddingValues ->

        if (serviceDetails == null && isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = UpdateRedTheme.Primary,
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Cargando datos del servicio...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = UpdateRedTheme.Light
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    UpdateRedTheme.Primary.copy(alpha = 0.2f),
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = null,
                                tint = UpdateRedTheme.Primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = "Modifica los campos que necesites cambiar",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                lineHeight = 20.sp
                            ),
                            color = UpdateRedTheme.Primary
                        )
                    }
                }

                OutlinedTextField(
                    value = tipo,
                    onValueChange = { tipo = it },
                    label = { Text("Tipo de Servicio") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = UpdateRedTheme.Primary,
                        focusedLabelColor = UpdateRedTheme.Primary
                    )
                )

                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    context,
                    { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth)
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        fecha = dateFormat.format(selectedDate.time)
                    }, year, month, day
                )

                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "Seleccionar fecha",
                                tint = UpdateRedTheme.Primary
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = UpdateRedTheme.Primary,
                        focusedLabelColor = UpdateRedTheme.Primary
                    )
                )

                OutlinedTextField(
                    value = costo,
                    onValueChange = { newValue ->
                        if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            costo = newValue
                        }
                    },
                    label = { Text("Costo ($)") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = UpdateRedTheme.Primary,
                        focusedLabelColor = UpdateRedTheme.Primary
                    )
                )

                OutlinedTextField(
                    value = taller,
                    onValueChange = { taller = it },
                    label = { Text("Taller") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = UpdateRedTheme.Primary,
                        focusedLabelColor = UpdateRedTheme.Primary
                    )
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp).padding(bottom = 24.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = UpdateRedTheme.Primary,
                        focusedLabelColor = UpdateRedTheme.Primary
                    )
                )


                fun hasChanges(): Boolean {
                    return tipo != originalTipo ||
                            fecha != originalFecha ||
                            costo != originalCosto ||
                            taller != originalTaller ||
                            descripcion != originalDescripcion
                }


                fun hasValidChanges(): Boolean {
                    if (!hasChanges()) return false


                    if (tipo != originalTipo && tipo.trim().isEmpty()) return false
                    if (costo != originalCosto && costo.isNotEmpty() && costo.toDoubleOrNull() == null) return false

                    return true
                }

                Button(
                    onClick = {
                        if (!hasChanges()) {
                            Toast.makeText(context, "No se han realizado cambios", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (!hasValidChanges()) {
                            Toast.makeText(context, "Los campos modificados deben tener valores válidos", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val costoDouble = if (costo.isNotEmpty()) costo.toDoubleOrNull() ?: originalCosto.toDouble() else originalCosto.toDouble()
                        updateServiceViewModel.updateService(
                            serviceId = serviceId,
                            newTipo = tipo,
                            newFecha = fecha,
                            newCosto = costoDouble,
                            newTaller = taller,
                            newDescripcion = descripcion
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hasValidChanges()) {
                            UpdateRedTheme.Primary
                        } else {
                            UpdateRedTheme.Primary.copy(alpha = 0.6f)
                        }
                    ),
                    enabled = !isLoading && hasValidChanges()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (hasValidChanges()) "Actualizar Servicio" else "Sin Cambios",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                if (hasValidChanges()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = UpdateRedTheme.Light
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "✅ Cambios detectados - Presiona el botón para guardar",
                            modifier = Modifier.padding(16.dp),
                            color = UpdateRedTheme.Primary,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                } else if (hasChanges()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "⚠️ Los campos modificados necesitan valores válidos",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF8FAFC)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "💡 Solo necesitas cambiar los campos que quieras actualizar",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}