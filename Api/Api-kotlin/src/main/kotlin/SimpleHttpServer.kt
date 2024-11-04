import java.io.*
import java.net.ServerSocket
import java.net.Socket
import kotlin.random.Random
import org.json.JSONObject

data class Task(val id: Int, val description: String)

val tasks = mutableListOf<Task>()

fun main() {
    val serverSocket = ServerSocket(8080)
    println("Servidor rodando na porta 8080")

    while (true) {
        val clientSocket = serverSocket.accept()
        handleClient(clientSocket)
    }
}

fun handleClient(clientSocket: Socket) {
    try {
        val inputStream = clientSocket.getInputStream()
        val reader = BufferedReader(InputStreamReader(inputStream))
        val outputStream = clientSocket.getOutputStream()
        val writer = PrintWriter(outputStream, true)

        val requestLine = reader.readLine()
        println("Recebido: $requestLine")

        val (method, path) = parseRequest(requestLine)

        var line: String?
        var contentLength = 0
        while (reader.readLine().also { line = it } != null) {
            if (line!!.isEmpty()) break
            if (line!!.startsWith("Content-Length:")) {
                contentLength = line!!.substringAfter(":").trim().toInt()
            }
        }

        val body = StringBuilder()
        if (contentLength > 0) {
            val buffer = CharArray(contentLength)
            reader.read(buffer, 0, contentLength)
            body.append(buffer)
        }

        when (method) {
            "GET" -> {
                when (path) {
                    "/tasks" -> {
                        writer.println("HTTP/1.1 200 OK")
                        writer.println("Content-Type: application/json")
                        writer.println()
                        writer.println(tasks.joinToString(prefix = "[", postfix = "]") { "{\"id\": ${it.id}, \"description\": \"${it.description}\"}" })
                    }
                    else -> {
                        writer.println("HTTP/1.1 404 Not Found")
                    }
                }
            }
            "POST" -> {
                val taskDescription = parseJsonBody(body.toString()) // Usa o corpo lido

                val newTask = Task(id = Random.nextInt(), description = taskDescription) // Gera um ID aleatório
                tasks.add(newTask)

                writer.println("HTTP/1.1 201 Created")
                writer.println("Content-Type: application/json")
                writer.println()
                writer.println("{\"id\": ${newTask.id}, \"description\": \"${newTask.description}\"}")
            }
            "PUT" -> {
                val id = path.split("/").last().toInt()
                val updatedDescription = parseJsonBody(body.toString())

                val taskIndex = tasks.indexOfFirst { it.id == id }
                if (taskIndex != -1) {
                    tasks[taskIndex] = tasks[taskIndex].copy(description = updatedDescription)

                    writer.println("HTTP/1.1 200 OK")
                    writer.println("Content-Type: application/json")
                    writer.println()
                    writer.println("{\"id\": $id, \"description\": \"${updatedDescription}\"}")
                } else {
                    writer.println("HTTP/1.1 404 Not Found")
                }
            }
            "DELETE" -> {
                val id = path.split("/").last().toInt()
                val taskIndex = tasks.indexOfFirst { it.id == id }
                if (taskIndex != -1) {
                    tasks.removeAt(taskIndex)

                    writer.println("HTTP/1.1 204 No Content")
                    writer.println("Content-Length: 0")
                    writer.println()

                    println("Tarefa com ID $id removida com sucesso.")
                } else {
                    writer.println("HTTP/1.1 404 Not Found")
                    writer.println("Content-Length: 0")
                    writer.println()

                    println("Tarefa com ID $id não encontrada.")
                }
            }


            else -> {
                writer.println("HTTP/1.1 405 Method Not Allowed")
            }
        }

        clientSocket.close()
    } catch (e: Exception) {
        println("Erro ao processar cliente: ${e.message}")
    }
}


fun parseJsonBody(body: String): String {
    val jsonObject = JSONObject(body)
    return jsonObject.getString("description") // Retorna a descrição da tarefa
}

fun parseRequest(requestLine: String): Pair<String, String> {
    val parts = requestLine.split(" ")
    return Pair(parts[0], parts[1])
}
