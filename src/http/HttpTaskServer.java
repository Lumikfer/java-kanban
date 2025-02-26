package http;

import com.sun.net.httpserver.HttpServer;
import http.handler.*;
import http.json.JsonTaskBuilder;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager taskManager;
    private final JsonTaskBuilder jsonTaskBuilder = new JsonTaskBuilder();

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.taskManager = taskManager;
    }

    public JsonTaskBuilder getJsonTaskBuilder() {
        return jsonTaskBuilder;
    }

    public void start() {
        server.createContext("/tasks", new TaskHandler(taskManager));
        server.createContext("/subtasks", new SubtaskHandler(taskManager));
        server.createContext("/epic", new EpicHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
        server.start();
    }

    public void stop() {
        server.stop(2);
    }

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        manager.addTask(new Task("dasdasdasd", "asdasdasd", Duration.ofMinutes(100), LocalDateTime.now()));
        manager.addTask(new Task("dasdasdasd11", "dasdasdasd11", Duration.ofMinutes(100), LocalDateTime.of(2025, 6, 2, 10, 10)));
        manager.addEpic(new Epic("dasdasdasd22", "dasdasdasd22"));
        manager.addSubtask(new Subtask("dasdasdasd33", "dasdasdasd33", 3, Duration.ofMinutes(100), LocalDateTime.of(2025, 6, 2, 10, 0)));
        manager.addSubtask(new Subtask("dasdasdasd212", "dasdasdasd212", 3, Duration.ofMinutes(100), LocalDateTime.of(2025, 7, 2, 10, 0)));
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
    }
}

