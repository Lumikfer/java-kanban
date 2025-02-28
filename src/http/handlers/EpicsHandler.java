package http.handlers;

import http.BaseHttpHandler;
import http.GsonFactory;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.*;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = GsonFactory.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    String query = exchange.getRequestURI().getQuery();
                    if (query != null) {
                        if (query.contains("id=") && query.contains("subtasks")) {
                            handleGetEpicSubtasks(exchange);
                        } else if (query.contains("id=")) {
                            handleGetEpicById(exchange);
                        } else {
                            handleGetEpics(exchange);
                        }
                    } else {
                        handleGetEpics(exchange);
                    }
                    break;
                case "POST":
                    handlePostEpic(exchange);
                    break;
                case "DELETE":
                    handleDeleteEpic(exchange);
                    break;
                default:
                    sendText(exchange, "Метод не поддерживается.", 405);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = taskManager.getEpics();
        String response = gson.toJson(epics);
        sendText(exchange, response, 200);
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query == null || !query.contains("id=")) {
            sendNotFound(exchange);
            return;
        }

        try {
            int id = Integer.parseInt(query.split("=")[1]);
            Epic epic = taskManager.getEpicByID(id);
            if (epic != null) {
                String response = gson.toJson(epic);
                sendText(exchange, response, 200);
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handleGetEpicSubtasks(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query == null || !query.contains("id=")) {
            sendNotFound(exchange);
            return;
        }

        try {
            int epicId = Integer.parseInt(query.split("=")[1]);
            List<Subtask> subtasks = taskManager.getEpicSubtasks(epicId);
            if (subtasks != null) {
                String response = gson.toJson(subtasks);
                sendText(exchange, response, 200);
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handlePostEpic(HttpExchange exchange) throws IOException {
        try {
            Epic epic = readRequestBody(exchange, Epic.class);
            if (epic.getId() == 0) {
                taskManager.addEpic(epic);
                String response = gson.toJson(epic);
                sendText(exchange, response, 201);
            } else {
                taskManager.updateEpic(epic);
                String response = gson.toJson(epic);
                sendText(exchange, response, 201);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query == null) {
            taskManager.deleteEpics();
            sendText(exchange, "Все эпики удалены.", 200);
        } else {
            try {
                int id = Integer.parseInt(query.split("=")[1]);
                taskManager.removeEpicByID(id);
                sendText(exchange, "Эпик удалён.", 200);
            } catch (NumberFormatException e) {
                sendNotFound(exchange);
            }
        }
    }
}