package solid;

import api.*;
import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.*;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTasksTest {
    private InMemoryTaskManager manager;
    private HttpTaskServer taskServer;
    private Gson gson;

    @BeforeEach
    void setUp() throws IOException {
        manager = new InMemoryTaskManager(); // Используем InMemoryTaskManager для тестов
        taskServer = new HttpTaskServer(manager); // Передаём менеджер в сервер
        taskServer.start(); // Запускаем сервер
        gson = GsonFactory.getGson(); // Инициализируем Gson для работы с JSON
    }

    @AfterEach
    void tearDown() {
        taskServer.stop(); // Останавливаем сервер после каждого теста
    }

    @Test
    void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Testing task addition", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.now());

        String taskJson = gson.toJson(task); // Сериализуем задачу в JSON

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Неверный код ответа при добавлении задачи");

        Task addedTask = gson.fromJson(response.body(), Task.class); // Десериализуем ответ
        assertNotNull(addedTask, "Задача не возвращается");

        List<Task> tasksFromManager = manager.getTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test Task", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void testUpdateTask() throws IOException, InterruptedException {
        // Добавляем задачу для обновления
        Task task = new Task("Test Task", "Testing task update", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.now());
        manager.addTask(task);

        // Обновляем задачу
        task.setName("Updated Task");
        task.setStatus(TaskStatus.IN_PROGRESS);
        String updatedTaskJson = gson.toJson(task);

        // Создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(updatedTaskJson))
                .build();

        // Отправляем запрос и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем код ответа
        assertEquals(201, response.statusCode(), "Неверный код ответа при обновлении задачи");

        // Проверяем, что задача обновилась в менеджере
        Task updatedTask = manager.getTaskByID(task.getId());
        assertNotNull(updatedTask, "Задача не найдена");
        assertEquals("Updated Task", updatedTask.getName(), "Некорректное имя задачи после обновления");
        assertEquals(TaskStatus.IN_PROGRESS, updatedTask.getStatus(), "Некорректный статус задачи после обновления");
    }

    @Test
    void testDeleteTask() throws IOException, InterruptedException {
        // Добавляем задачу для удаления
        Task task = new Task("Test Task", "Testing task deletion", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.now());
        manager.addTask(task);

        // Создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?id=" + task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // Отправляем запрос и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем код ответа
        assertEquals(200, response.statusCode(), "Неверный код ответа при удалении задачи");

        // Проверяем, что задача удалилась из менеджера
        List<Task> tasksFromManager = manager.getTasks();
        assertTrue(tasksFromManager.isEmpty(), "Задача не удалена");
    }

    @Test
    void testGetTaskById() throws IOException, InterruptedException {
        // Добавляем задачу для получения
        Task task = new Task("Test Task", "Testing task retrieval", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.now());
        manager.addTask(task);

        // Создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?id=" + task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // Отправляем запрос и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем код ответа
        assertEquals(200, response.statusCode(), "Неверный код ответа при получении задачи");

        // Проверяем, что задача возвращается корректно
        Task retrievedTask = gson.fromJson(response.body(), Task.class);
        assertNotNull(retrievedTask, "Задача не возвращается");
        assertEquals(task.getId(), retrievedTask.getId(), "Некорректный ID задачи");
        assertEquals(task.getName(), retrievedTask.getName(), "Некорректное имя задачи");
    }
}