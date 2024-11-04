package com.example.fiapkt.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fiapkt.R

@Composable
fun loginScreen(navController: NavController, context: Context) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        username = getData(context, "username", "")
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorFundo)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo do app",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .offset(y = (-60).dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .offset(y = (-50).dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(30.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Username", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .background(Color.Gray.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = username,
                        onValueChange = { username = it },
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 21.sp,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Password", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .background(Color.Gray.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = password,
                        onValueChange = { password = it },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 21.sp,
                        )
                    )
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (username == "admin" && password == "123456") {
                            saveData(context, "username", username)
                            navController.navigate("home")
                        } else {
                            showSnackbar = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
                ) {
                    Text(text = "Login", color = Color.White)
                }

                if (showSnackbar) {
                    SnackbarHost(snackbarHostState) {
                        Snackbar(
                            modifier = Modifier.padding(16.dp),
                            containerColor = Color.White,
                            contentColor = Color.Black,
                            action = {
                                TextButton(onClick = { showSnackbar = false }) {
                                    Text("Fechar", color = Color.Black)
                                }
                            }
                        ) {
                            Text("Credenciais inválidas. Tente novamente.", color = Color.Black)
                        }
                    }
                }
            }
        }
    }

    if (showSnackbar) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar("Credenciais inválidas. Tente novamente.")
            showSnackbar = false
        }
    }
}

fun saveData(context: Context, key: String, value: String) {
    val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(key, value)
    editor.apply()
}

fun getData(context: Context, key: String, defaultValue: String): String {
    val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, defaultValue) ?: defaultValue
}

fun removeData(context: Context, key: String) {
    val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.remove(key)
    editor.apply()
}
