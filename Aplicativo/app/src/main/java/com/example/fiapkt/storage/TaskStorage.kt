package com.example.fiapkt.storage

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.fiapkt.model.Task
import com.google.gson.Gson

val Context.dataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(name = "tasks_prefs")

class TaskStorage(private val context: Context) {

    private val TASKS_KEY = stringPreferencesKey("tasks")

    suspend fun saveTasks(tasks: List<Task>) {
        val tasksString = Gson().toJson(tasks)
        context.dataStore.edit { prefs ->
            prefs[TASKS_KEY] = tasksString
        }
    }

    val tasksFlow: Flow<List<Task>> = context.dataStore.data
        .map { prefs ->
            val tasksString = prefs[TASKS_KEY] ?: "[]"
            Gson().fromJson(tasksString, Array<Task>::class.java).toList()
        }
}
