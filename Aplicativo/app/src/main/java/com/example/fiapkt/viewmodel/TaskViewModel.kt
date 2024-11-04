package com.example.fiapkt.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fiapkt.model.Task
import com.example.fiapkt.network.RetrofitInstance
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.runtime.mutableStateListOf

class TaskViewModel(private val context: Context) : ViewModel() {
    private val _tasks = mutableStateListOf<Task>()
    val tasks: List<Task> get() = _tasks

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getTasks()
                if (response.isSuccessful) {
                    response.body()?.let { taskList ->
                        _tasks.clear()
                        _tasks.addAll(taskList)
                    }
                }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Erro ao carregar tarefas: ${e.message}")
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.createTask(task)
                if (response.isSuccessful) {
                    response.body()?.let { newTask ->
                        _tasks.add(newTask)
                    }
                }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Erro ao adicionar tarefa: ${e.message}")
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.updateTask(task.id, task)
                if (response.isSuccessful) {
                    response.body()?.let { updatedTask ->
                        val index = _tasks.indexOfFirst { it.id == updatedTask.id }
                        if (index != -1) {
                            _tasks[index] = updatedTask
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Erro ao atualizar tarefa: ${e.message}")
            }
        }
    }

    fun removeTask(taskId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.deleteTask(taskId)
                if (response.isSuccessful) {
                    _tasks.removeAll { it.id == taskId }
                }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Erro ao remover tarefa: ${e.message}")
            }
        }
    }

}
