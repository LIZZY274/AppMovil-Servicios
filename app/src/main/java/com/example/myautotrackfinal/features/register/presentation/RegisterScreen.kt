package com.example.myautotrackfinal.features.register.presentation

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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.graphicsLayer
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
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, registerViewModel: RegisterViewModel = viewModel()) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val registerSuccess by registerViewModel.registerSuccess.observeAsState()
    val errorMessage by registerViewModel.errorMessage.observeAsState()
    val isLoading by registerViewModel.isLoading.observeAsState(false)

    LaunchedEffect(registerSuccess) {
        if (registerSuccess == true) {
            Toast.makeText(context, "✅ Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
            navController.navigate(Screens.Login.route) {
                popUpTo(Screens.Register.route) { inclusive = true }
            }
            registerViewModel.clearMessages()
        }
    }
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, "❌ $it", Toast.LENGTH_LONG).show()
            registerViewModel.clearMessages()
        }
    }

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
            Spacer(modifier = Modifier.height(20.dp))

            SportsCarWithFireLogo()

            Spacer(modifier = Modifier.height(36.dp))

            ProfessionalRegisterField(
                value = name,
                onValueChange = { name = it },
                label = "Nombre completo",
                placeholder = "Juan Pérez",
                icon = Icons.Default.Person,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfessionalRegisterField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "usuario@ejemplo.com",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfessionalRegisterPasswordField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                placeholder = "Mínimo 6 caracteres",
                passwordVisibility = passwordVisible,
                onVisibilityChange = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfessionalRegisterPasswordField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirmar contraseña",
                placeholder = "Repite tu contraseña",
                passwordVisibility = confirmPasswordVisible,
                onVisibilityChange = { confirmPasswordVisible = !confirmPasswordVisible }
            )

            Spacer(modifier = Modifier.height(28.dp))

            ProfessionalRegisterButton(
                isLoading = isLoading,
                onClick = {
                    when {
                        name.isEmpty() -> Toast.makeText(context, "👤 Ingresa tu nombre", Toast.LENGTH_SHORT).show()
                        email.isEmpty() -> Toast.makeText(context, "📧 Ingresa tu email", Toast.LENGTH_SHORT).show()
                        password.isEmpty() -> Toast.makeText(context, "🔒 Ingresa una contraseña", Toast.LENGTH_SHORT).show()
                        confirmPassword.isEmpty() -> Toast.makeText(context, "🔒 Confirma tu contraseña", Toast.LENGTH_SHORT).show()
                        password != confirmPassword -> Toast.makeText(context, "❌ Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                        password.length < 6 -> Toast.makeText(context, "⚠️ La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                        else -> registerViewModel.register(name, email, password)
                    }
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes cuenta? ",
                    color = Color(0xFF6B7280),
                    fontSize = 15.sp
                )
                Text(
                    text = "Inicia sesión",
                    color = Color(0xFFDC2626),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        navController.navigate(Screens.Login.route)
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SportsCarWithFireLogo() {
    val infiniteTransition = rememberInfiniteTransition(label = "sports_car")


    val fireOpacity by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(300),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fire_flicker"
    )


    val carShake by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(100),
            repeatMode = RepeatMode.Reverse
        ),
        label = "car_shake"
    )


    val smokeScale by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "smoke_scale"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier
                .size(140.dp)
                .shadow(12.dp, CircleShape),
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFDC2626).copy(alpha = 0.1f),
                                Color(0xFF000000).copy(alpha = 0.05f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "💨",
                    fontSize = (16 + smokeScale * 8).sp,
                    modifier = Modifier
                        .offset(x = (-20).dp, y = 5.dp)
                        .graphicsLayer(alpha = 0.6f)
                )

                Text(
                    text = "🏎️",
                    fontSize = 48.sp,
                    modifier = Modifier
                        .offset(x = carShake.dp, y = carShake.dp)
                )

                Text(
                    text = "🔥",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .offset(x = (-25).dp, y = 0.dp)
                        .graphicsLayer(alpha = fireOpacity)
                )

                Text(
                    text = "✨",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .offset(x = (-30).dp, y = (-10).dp)
                        .graphicsLayer(alpha = fireOpacity * 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "CREAR CUENTA",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 3.sp,
            color = Color(0xFFDC2626)
        )
    }
}

@Composable
fun ProfessionalRegisterField(
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
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF374151),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = Color(0xFF9CA3AF),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        icon,
                        contentDescription = label,
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(20.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFDC2626),
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color(0xFF111827),
                    unfocusedTextColor = Color(0xFF111827)
                )
            )
        }
    }
}

@Composable
fun ProfessionalRegisterPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    passwordVisibility: Boolean,
    onVisibilityChange: () -> Unit
) {
    Column {
        Text(
            text = label.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF374151),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = Color(0xFF9CA3AF),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onVisibilityChange) {
                        Icon(
                            if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility",
                            tint = Color(0xFF6B7280),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFDC2626),
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color(0xFF111827),
                    unfocusedTextColor = Color(0xFF111827)
                )
            )
        }
    }
}

@Composable
fun ProfessionalRegisterButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(6.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
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
                    modifier = Modifier.size(18.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Creando cuenta...",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            Text(
                text = "CREAR CUENTA",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}