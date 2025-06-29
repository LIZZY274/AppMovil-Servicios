package com.example.myautotrackfinal.features.login.presentation

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer  // ← IMPORT AGREGADO
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myautotrackfinal.core.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    val loginSuccess by loginViewModel.loginSuccess.observeAsState()
    val errorMessage by loginViewModel.errorMessage.observeAsState()
    val isLoading by loginViewModel.isLoading.observeAsState(false)

    // Observar el éxito del login
    LaunchedEffect(loginSuccess) {
        if (loginSuccess == true) {
            Toast.makeText(context, "✅ Bienvenido a AutoTrack", Toast.LENGTH_SHORT).show()
            navController.navigate(Screens.Home.route) {
                popUpTo(Screens.Login.route) { inclusive = true }
            }
            loginViewModel.clearMessages()
        }
    }

    // Observar mensajes de error
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, "❌ $it", Toast.LENGTH_LONG).show()
            loginViewModel.clearMessages()
        }
    }

    // 🎨 FONDO CON GRADIENTE PROFESIONAL
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFAFAFA),
                        Color(0xFFFFFFFF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // 🚗 LOGO ANIMADO CON CARRO
            AnimatedCarLogo()

            Spacer(modifier = Modifier.height(48.dp))

            // 📧 CAMPO EMAIL PROFESIONAL
            ProfessionalTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "usuario@ejemplo.com",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🔒 CAMPO PASSWORD PROFESIONAL
            ProfessionalPasswordField(
                value = password,
                onValueChange = { password = it },
                passwordVisibility = passwordVisibility,
                onVisibilityChange = { passwordVisibility = !passwordVisibility }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔗 FORGOT PASSWORD
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color(0xFFDC2626),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { Toast.makeText(context, "🔄 Función próximamente", Toast.LENGTH_SHORT).show() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 🔴 BOTÓN LOGIN PROFESIONAL
            ProfessionalLoginButton(
                isLoading = isLoading,
                onClick = {
                    when {
                        email.isEmpty() -> Toast.makeText(context, "📧 Ingresa tu email", Toast.LENGTH_SHORT).show()
                        password.isEmpty() -> Toast.makeText(context, "🔒 Ingresa tu contraseña", Toast.LENGTH_SHORT).show()
                        else -> loginViewModel.login(email, password)
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 📝 REGISTRO
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿No tienes cuenta? ",
                    color = Color(0xFF6B7280),
                    fontSize = 15.sp
                )
                Text(
                    text = "Regístrate aquí",
                    color = Color(0xFFDC2626),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        navController.navigate(Screens.Register.route)
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// 🚗 LOGO ANIMADO CON CARRO MOVIÉNDOSE
@Composable
fun AnimatedCarLogo() {
    // 🎭 ANIMACIÓN DEL CARRO - Se mueve de izquierda a derecha infinitamente
    val infiniteTransition = rememberInfiniteTransition(label = "car_animation")

    // Posición horizontal del carro
    val carPosition by infiniteTransition.animateFloat(
        initialValue = -50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "car_position"
    )

    // Rotación sutil del carro
    val carRotation by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "car_rotation"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // 🎨 CONTENEDOR DEL LOGO
        Card(
            modifier = Modifier
                .size(140.dp)
                .shadow(12.dp, CircleShape),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFDC2626).copy(alpha = 0.1f),
                                Color(0xFFDC2626).copy(alpha = 0.05f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // 🚗 CARRO ANIMADO
                Box(
                    modifier = Modifier.offset(x = carPosition.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🚗",
                        fontSize = 48.sp,
                        modifier = Modifier.graphicsLayer(rotationZ = carRotation) // ← CORREGIDO
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🏷️ TÍTULO PROFESIONAL
        Text(
            text = "AUTOTRACK",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 3.sp,
            color = Color(0xFFDC2626),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Tu taller de confianza",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF6B7280),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

// 📧 CAMPO DE TEXTO PROFESIONAL
@Composable
fun ProfessionalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType
) {
    Column {
        Text(
            text = label.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF374151),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = Color(0xFF9CA3AF)
                    )
                },
                leadingIcon = {
                    Icon(
                        icon,
                        contentDescription = label,
                        tint = Color(0xFFDC2626)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFDC2626),     // ← CORREGIDO
                    unfocusedBorderColor = Color.Transparent,   // ← CORREGIDO
                    focusedTextColor = Color(0xFF111827),       // ← CORREGIDO
                    unfocusedTextColor = Color(0xFF111827)      // ← CORREGIDO
                )
            )
        }
    }
}

// 🔒 CAMPO PASSWORD PROFESIONAL
@Composable
fun ProfessionalPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    passwordVisibility: Boolean,
    onVisibilityChange: () -> Unit
) {
    Column {
        Text(
            text = "CONTRASEÑA",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF374151),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = "Tu contraseña segura",
                        color = Color(0xFF9CA3AF)
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = Color(0xFFDC2626)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onVisibilityChange) {
                        Icon(
                            if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility",
                            tint = Color(0xFF6B7280)
                        )
                    }
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFDC2626),     // ← CORREGIDO
                    unfocusedBorderColor = Color.Transparent,   // ← CORREGIDO
                    focusedTextColor = Color(0xFF111827),       // ← CORREGIDO
                    unfocusedTextColor = Color(0xFF111827)      // ← CORREGIDO
                )
            )
        }
    }
}

// 🔴 BOTÓN LOGIN PROFESIONAL
@Composable
fun ProfessionalLoginButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFDC2626),
            contentColor = Color.White
        ),
        enabled = !isLoading
    ) {
        if (isLoading) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Iniciando sesión...",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            Text(
                text = "INICIAR SESIÓN",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}