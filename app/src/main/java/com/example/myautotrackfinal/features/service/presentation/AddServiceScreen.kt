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
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.text.font.FontWeight
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


    LaunchedEffect(addServiceSuccess) {
        if (addServiceSuccess == true) {
            Toast.makeText(context, "✅ Servicio agregado exitosamente", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
            addServiceViewModel.clearMessages()
        }
    }


    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            addServiceViewModel.clearMessages()
        }
    }


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
                title = { Text("➕ Agregar Nuevo Servicio", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE53E3E))
            )
        },
        modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAFC))
    ) { paddingValues ->
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
                    containerColor = Color(0xFFE53E3E).copy(alpha = 0.1f)
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
                                Color(0xFFE53E3E).copy(alpha = 0.2f),
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = Color(0xFFE53E3E),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Completa la información para crear un nuevo servicio",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 20.sp
                        ),
                        color = Color(0xFFE53E3E)
                    )
                }
            }

            OutlinedTextField(
                value = tipo,
                onValueChange = { tipo = it },
                label = { Text("Tipo de Servicio *") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE53E3E),
                    focusedLabelColor = Color(0xFFE53E3E)
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
                label = { Text("Fecha (YYYY-MM-DD) *") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE53E3E),
                    focusedLabelColor = Color(0xFFE53E3E)
                )
            )

            OutlinedTextField(
                value = costo,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        costo = newValue
                    }
                },
                label = { Text("Costo *") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE53E3E),
                    focusedLabelColor = Color(0xFFE53E3E)
                )
            )

            OutlinedTextField(
                value = taller,
                onValueChange = { taller = it },
                label = { Text("Taller *") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE53E3E),
                    focusedLabelColor = Color(0xFFE53E3E)
                )
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (Opcional)") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp).padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE53E3E),
                    focusedLabelColor = Color(0xFFE53E3E)
                )
            )


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
                        text = "📸 Foto del Vehículo (Opcional)",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (capturedImageUri != null) {
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

                        OutlinedButton(
                            onClick = { addServiceViewModel.showCamera() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = "Cámara")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tomar nueva foto")
                        }
                    } else {
                        Button(
                            onClick = { addServiceViewModel.showCamera() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE53E3E)
                            )
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = "Cámara")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tomar foto")
                        }
                    }
                }
            }


            Button(
                onClick = {
                    val costoDouble = costo.toDoubleOrNull()
                    if (tipo.isNotEmpty() && fecha.isNotEmpty() && costoDouble != null && taller.isNotEmpty()) {
                        addServiceViewModel.addService(tipo, fecha, costoDouble, taller, descripcion)
                    } else {
                        Toast.makeText(context, "⚠️ Por favor, completa todos los campos obligatorios (*)", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53E3E)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text(text = "➕ Agregar Servicio", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            // MOSTRAR ESTADO DE LA IMAGEN
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


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text = "ℹ️ Los campos marcados con (*) son obligatorios",
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}