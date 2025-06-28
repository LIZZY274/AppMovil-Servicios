package com.example.myautotrackfinal.features.service.presentation

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateServiceScreen(navController: NavController, serviceId: String, updateServiceViewModel: UpdateServiceViewModel = viewModel()) {
    val context = LocalContext.current

    var tipo by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var costo by remember { mutableStateOf("") }
    var taller by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // ✅ CORREGIDO: Usar los nombres correctos del ViewModel
    val serviceDetails by updateServiceViewModel.service.observeAsState()
    val updateServiceSuccess by updateServiceViewModel.updateSuccess.observeAsState()
    val errorMessage by updateServiceViewModel.errorMessage.observeAsState()
    val isLoading by updateServiceViewModel.isLoading.observeAsState(false)

    // Cargar detalles del servicio cuando la pantalla se compone o el serviceId cambia
    LaunchedEffect(serviceId) {
        updateServiceViewModel.loadService(serviceId) // ✅ CORREGIDO: Método correcto
    }

    // Actualizar los campos de texto cuando los detalles del servicio son cargados
    LaunchedEffect(serviceDetails) {
        serviceDetails?.let { service -> // ✅ CORREGIDO: Dar nombre a la variable
            tipo = service.tipo
            fecha = service.fecha
            costo = service.costo.toString()
            taller = service.taller
            descripcion = service.descripcion ?: "" // ✅ CORRECCIÓN: Manejar el null con operador Elvis
        }
    }

    // Observar el éxito de la actualización
    LaunchedEffect(updateServiceSuccess) {
        if (updateServiceSuccess == true) {
            Toast.makeText(context, "Servicio actualizado exitosamente", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
            updateServiceViewModel.clearMessages()
        }
    }

    // Observar mensajes de error
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            updateServiceViewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modificar Servicio", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        if (serviceDetails == null && isLoading) {
            // Mostrar loading mientras se cargan los datos
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = tipo,
                    onValueChange = { tipo = it },
                    label = { Text("Tipo de Servicio") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    shape = RoundedCornerShape(8.dp)
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
                    label = { Text("Fecha (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                        }
                    },
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = costo,
                    onValueChange = { newValue ->
                        if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            costo = newValue
                        }
                    },
                    label = { Text("Costo") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = taller,
                    onValueChange = { taller = it },
                    label = { Text("Taller") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp).padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    shape = RoundedCornerShape(8.dp)
                )

                Button(
                    onClick = {
                        val costoDouble = costo.toDoubleOrNull()
                        if (tipo.isNotEmpty() && fecha.isNotEmpty() && costoDouble != null && taller.isNotEmpty()) {
                            updateServiceViewModel.updateService(serviceId, tipo, fecha, costoDouble, taller, descripcion)
                        } else {
                            Toast.makeText(context, "Por favor, completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text(text = "Actualizar Servicio", fontSize = 18.sp, color = Color.White)
                    }
                }
            }
        }
    }
}