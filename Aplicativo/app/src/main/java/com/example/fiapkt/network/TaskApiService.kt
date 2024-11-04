package com.example.fiapkt.network

import com.example.fiapkt.model.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("/tasks")
    suspend fun getTasks(): Response<List<Task>>

    @POST("/tasks")
    suspend fun createTask(@Body task: Task): Response<Task>

    @PUT("/tasks/{id}")
    suspend fun updateTask(@Path("id") id: Int, @Body task: Task): Response<Task>

    @DELETE("/tasks/{id}")
    suspend fun deleteTask(@Path("id") taskId: Int): Response<Unit>
}
