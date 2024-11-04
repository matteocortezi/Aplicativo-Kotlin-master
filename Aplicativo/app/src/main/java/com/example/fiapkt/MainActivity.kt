package com.example.fiapkt

import SplashScreen
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fiapkt.screens.MainScreen
import com.example.fiapkt.screens.loginScreen
import com.example.fiapkt.ui.theme.FiapKtTheme
import com.example.fiapkt.viewmodel.TaskViewModel
import com.example.fiapkt.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = TaskViewModelFactory(applicationContext)
        taskViewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)

        enableEdgeToEdge()
        setContent {
            FiapKtTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "splash") {
                        composable(route = "splash") {
                            SplashScreen(navController = navController)
                        }
                        composable(route = "login") {
                            val context = LocalContext.current
                            loginScreen(navController, context)
                        }
                        composable(route = "home") {
                            MainScreen(navController = navController, taskViewModel = taskViewModel)
                        }
                    }
                }
            }
        }
    }
}
