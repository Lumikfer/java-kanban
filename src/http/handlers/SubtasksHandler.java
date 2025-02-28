package http.handlers;

import http.BaseHttpHandler;
import http.GsonFactory;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.*;
import tasks.Subtask;

import java.io.IOException;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtasksHandler(TaskManager taskManager) {
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
                    if (query != null && query.contains("id=")) {
                        handleGetSubtaskById(exchange);
                    } else {
                        handleGetSubtasks(exchange);
                    }
                    break;
                case "POST":
                    handlePostSubtask(exchange);
                    break;
                case "DELETE":
                    handleDeleteSubtask(exchange);
                    break;
                default:
                    sendText(exchange, "Метод не поддерживается.", 405);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> subtasks = taskManager.getSubtasks();
        String response = gson.toJson(subtasks);
        sendText(exchange, response, 200);
    }

    private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query == null || !query.contains("id=")) {
            sendNotFound(exchange);
            return;
        }

        try {
            int id = Integer.parseInt(query.split("=")[1]);
            Subtask subtask = taskManager.getSubtaskByID(id);
            if (subtask != null) {
                String response = gson.toJson(subtask);
                sendText(exchange, response, 200);
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handlePostSubtask(HttpExchange exchange) throws IOException {
        try {
            Subtask subtask = readRequestBody(exchange, Subtask.class);

            if (subtask.getStartTime() != null && taskManager.isTaskOverlapping(subtask, subtask.getId())) {
                sendHasInteractions(exchange); // Отправляем 406, если подзадача пересекается
                return;
            }

            if (subtask.getId() == 0) {
                taskManager.addSubtask(subtask);
                String response = gson.toJson(subtask);
                sendText(exchange, response, 201);
            } else {
                taskManager.updateSubtask(subtask);
                String response = gson.toJson(subtask);
                sendText(exchange, response, 201);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query == null) {
            taskManager.deleteSubtasks();
            sendText(exchange, "Все подзадачи удалены.", 200);
        } else {
            try {
                int id = Integer.parseInt(query.split("=")[1]);
                taskManager.removeSubtaskByID(id);
                sendText(exchange, "Подзадача удалена.", 200);
            } catch (NumberFormatException e) {
                sendNotFound(exchange);
            }
        }
    }
}