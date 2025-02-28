package http;

import http.handlers.*;
import com.sun.net.httpserver.HttpServer;
import manager.*;


import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    public static String[] args;

    // Конструктор с возможностью передачи TaskManager
    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new http.handlers.TasksHandler(taskManager));
        server.createContext("/subtasks", new http.handlers.SubtasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new http.handlers.PrioritizedHandler(taskManager));
    }

    // Метод для запуска сервера
    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на порту " + PORT);
    }

    // Метод для остановки сервера
    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен.");
    }

    // Метод main для запуска сервера с дефолтным менеджером
    public static void main(String[] args) throws IOException {
        HttpTaskServer.args = args;
        TaskManager taskManager = Managers.getDefault(); // Используем дефолтный менеджер
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }
}