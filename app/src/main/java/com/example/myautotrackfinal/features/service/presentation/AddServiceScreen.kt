package com.example.myautotrackfinal.features.service.presentation

import android.app.DatePickerDialog
import android.net.Uri
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceScreen(navController: NavController, addServiceViewModel: AddServiceViewModel = viewModel()) {
    val context = LocalContext.current

    var tipo by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var costo by remember { mutableStateOf("") }
    var taller by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    val addServiceSuccess by addServiceViewModel.addServiceSuccess.observeAsState()
    val errorMessage by addServiceViewModel.errorMessage.observeAsState()
    val isLoading by addServiceViewModel.isLoading.observeAsState(false)
    val showCamera by addServiceViewModel.showCamera.observeAsState(false)
    val capturedImageUri by addServiceViewModel.capturedImageUri.observeAsState()

    // Observar el éxito de agregar servicio
    LaunchedEffect(addServiceSuccess) {
        if (addServiceSuccess == true) {
            Toast.makeText(context, "Servicio agregado exitosamente", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
            addServiceViewModel.clearMessages()
        }
    }

    // Observar mensajes de error
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            addServiceViewModel.clearMessages()
        }
    }

    // ⭐ MOSTRAR PANTALLA DE CÁMARA
    if (showCamera == true) {
        CameraScreen(
            onPhotoCaptured = { uri ->
                addServiceViewModel.setImageUri(uri)
                addServiceViewModel.hideCamera()
                Toast.makeText(context, "📸 Foto capturada exitosamente", Toast.LENGTH_SHORT).show()
            },
            onBackPressed = {
                addServiceViewModel.hideCamera()
            },
            cameraRepository = addServiceViewModel.getCameraRepository()
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Nuevo Servicio", color = Color.White) },
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
                label = { Text("Descripción (Opcional)") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp).padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = RoundedCornerShape(8.dp)
            )

            // ⭐ SECCIÓN DE CÁMARA
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "📸 Foto del Vehículo",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (capturedImageUri != null) {
                        // ✅ MOSTRAR IMAGEN CAPTURADA
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(capturedImageUri),
                                contentDescription = "Foto del vehículo",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            // Botón para eliminar imagen
                            IconButton(
                                onClick = { addServiceViewModel.removeImage() },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .background(
                                        Color.Red,
                                        RoundedCornerShape(50)
                                    )
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar foto",
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Botón para tomar nueva foto
                        OutlinedButton(
                            onClick = { addServiceViewModel.showCamera() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = "Cámara")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tomar nueva foto")
                        }
                    } else {
                        // ⭐ BOTÓN PARA TOMAR FOTO
                        Button(
                            onClick = { addServiceViewModel.showCamera() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = "Cámara")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("")
                        }
                    }
                }
            }

            // ⭐ BOTÓN AGREGAR SERVICIO
            Button(
                onClick = {
                    val costoDouble = costo.toDoubleOrNull()
                    if (tipo.isNotEmpty() && fecha.isNotEmpty() && costoDouble != null && taller.isNotEmpty()) {
                        addServiceViewModel.addService(tipo, fecha, costoDouble, taller, descripcion)
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
                    Text(text = "Agregar Servicio", fontSize = 18.sp, color = Color.White)
                }
            }

            // ⭐ MOSTRAR ESTADO DE LA IMAGEN
            if (capturedImageUri != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Green.copy(alpha = 0.1f))
                ) {
                    Text(
                        text = "✅ Foto guardada - Se incluirá con el servicio",
                        modifier = Modifier.padding(12.dp),
                        color = Color.Green.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}