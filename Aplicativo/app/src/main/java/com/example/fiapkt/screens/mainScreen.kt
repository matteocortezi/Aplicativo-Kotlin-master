package com.example.fiapkt.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fiapkt.R
import com.example.fiapkt.model.Task
import com.example.fiapkt.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, taskViewModel: TaskViewModel) {
    var taskDescription by remember { mutableStateOf("") }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorFundo))
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.colorFundoTop)
            ),
            modifier = Modifier.fillMaxWidth(),
            title = {
                Box(
                    modifier = Modifier

                        .padding(vertical = 8.dp), // Ajuste o padding vertical
                    contentAlignment = Alignment.Center
                ) {

                }
            },
            navigationIcon = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp), // Ajuste o padding vertical
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(50.dp)
                    )
                }
              },
            actions = {
                IconButton(onClick = { navController.navigate("login") }) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Sair da conta",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 100.dp), // Ajuste o padding superior para evitar sobreposição com a TopAppBar
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = taskDescription,
                        onValueChange = { taskDescription = it },
                        label = { Text("Descrição da Tarefa") }
                    )
                    Button(
                        onClick = {
                            taskToEdit?.let {
                                taskViewModel.updateTask(it.copy(description = taskDescription))
                                taskToEdit = null
                            } ?: run {
                                taskViewModel.addTask(Task(id = 0, description = taskDescription))
                            }
                            taskDescription = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
                    ) {
                        Text(if (taskToEdit != null) "Atualizar Tarefa" else "Adicionar Tarefa")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(taskViewModel.tasks) { task ->
                    TaskItem(
                        task = task,
                        onEdit = {
                            taskDescription = task.description
                            taskToEdit = task
                        },
                        onRemove = {
                            taskViewModel.removeTask(task.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onEdit: () -> Unit, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyLarge
            )
            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar Tarefa"
                    )
                }
                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remover Tarefa"
                    )
                }
            }
        }
    }
}
