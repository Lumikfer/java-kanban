package TestHttp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.DurationTimeAdapter;
import http.HttpTaskServer;
import http.LocalDateTimeAdapter;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import statuses.StatusTask;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson =  new GsonBuilder()
            .registerTypeAdapter(Duration.class,new DurationTimeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {

        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test 2", "Testing task 2",
                StatusTask.NEW, 1,LocalDateTime.now());

        // конвертируем её в JSON
        String taskJson = gson.toJson(task);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }
    @Test
    public void EpicAddTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Test epic", "Testing epic 2",
                StatusTask.NEW, 2,LocalDateTime.now());
        String epicjs = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicjs)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        // проверяем, что создалась одна задача с корректным именем
        List<Epic> tasksFromManager = manager.getEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test epic", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
        System.out.println(tasksFromManager);

    }

    @Test
    public void SubtaskAddTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Test epic", "Testing epic 2",
                StatusTask.NEW, 1,LocalDateTime.now());
        Subtask sub = new Subtask("Sub1","test sub1",StatusTask.NEW,1,3,LocalDateTime.now());
        Subtask sub1 = new Subtask("Subb1","test subb1",StatusTask.NEW,1,4,LocalDateTime.now());
        manager.addEpic(epic);
        manager.addSubtask(sub);
        manager.addSubtask(sub1);

        String subjs = gson.toJson(sub);
        String subjs2 = gson.toJson(sub1);
        String epicjs = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subjs)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> tasksFromManager = manager.getSubtasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        System.out.println(tasksFromManager);

    }
    @Test
    public void DeleteTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Test epic", "Testing epic 2",
                StatusTask.NEW, 1,LocalDateTime.now());
        Subtask sub = new Subtask("Sub1","test sub1",StatusTask.NEW,1,3,LocalDateTime.now());
        Subtask sub1 = new Subtask("Subb1","test subb1",StatusTask.NEW,1,4,LocalDateTime.now());
        manager.addEpic(epic);
        manager.addSubtask(sub);
        manager.addSubtask(sub1);

        String subjs = gson.toJson(sub);
        String subjs2 = gson.toJson(sub1);
        String epicjs = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        manager.deleteEpic(1);
        assertEquals(200, response.statusCode());
        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> tasksFromManager = manager.getSubtasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
        System.out.println(tasksFromManager);
    }

}