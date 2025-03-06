package http;

import com.sun.net.httpserver.HttpServer;
import managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;


import static util.Managers.getDefault;

public class HttpTaskServer {

    private static final int PORT = 8080;

    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        httpServer.createContext("/tasks", new TaskHttpHandler(taskManager));
        httpServer.createContext("/subtasks", new SubTaskHttpHandler(taskManager));
        httpServer.createContext("/epics", new EpicHttpHendler(taskManager));
        httpServer.createContext("/history", new Histhttphandler(taskManager));
        httpServer.createContext("/prioritized", new PriorHttpHandler(taskManager));
    }

    public static void main(String[] args) {
        TaskManager taskManager = getDefault();
        HttpTaskServer httpServer = new HttpTaskServer(taskManager);
        httpServer.start();
        httpServer.stop();
    }

    public void start() {
        httpServer.start();
        System.out.println("сервер запущен");
    }

    public void stop() {
        httpServer.stop();
        System.out.println("сервер остановлен");
    }
}