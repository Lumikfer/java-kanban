package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.*;
import tasks.Epic;

import java.io.IOException;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS: {
                handleGetTask(exchange);
                break;
            }
            case GET_TASK_BY_ID: {
                handleGetTaskById(exchange);
                break;
            }
            case POST_TASK: {
                handlePostTask(exchange);
                break;
            }
            case DELETE_TASK: {
                handleDeleteTask(exchange);
                break;
            }
            case GET_SUBTASKS_BY_EPIC_ID: {
                handleGetSubtasksByEpic(exchange);
                break;
            }
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 2 && pathParts[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASKS;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_TASK;
            }
        }
        if (pathParts.length == 3 && pathParts[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASK_BY_ID;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_TASK;
            }
        }
        if (pathParts.length == 4 && pathParts[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_SUBTASKS_BY_EPIC_ID;
            }
        }
        return Endpoint.UNKNOWN;
    }

    private void handleGetTask(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getEpics());
        if (response.equals("[]")) {
            sendNotFound(exchange, "Список задач пуст");
        } else {
            sendText(exchange, response, 200);
        }
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        Epic newEpic = gson.fromJson(requestBody, Epic.class);
        if (newEpic.getId() > 0 && taskManager.getEpic(newEpic.getId()) != null) {
            taskManager.updateEpic(newEpic);
            sendText(exchange, "Задача с ID=" + newEpic.getId() + " обновлена", 201);
        } else {
            int taskId = taskManager.addEpic(newEpic);
            if (taskId > 0) {
                sendText(exchange, "Задача добавлена с ID: " + taskId, 201);
            } else {
                sendHasInteractions(exchange);
            }
        }
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        String[] requestURI = exchange.getRequestURI().getPath().split("/");
        String response = gson.toJson(taskManager.getEpic(Integer.parseInt(requestURI[2])));
        if (response.equals("null")) {
            sendNotFound(exchange, "Такой задачи нет");
        } else {
            sendText(exchange, response, 200);
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        String[] requestURI = exchange.getRequestURI().getPath().split("/");
        if (taskManager.getEpic(Integer.parseInt(requestURI[2])) != null) {
            taskManager.deleteEpic(Integer.parseInt(requestURI[2]));
            sendText(exchange, "Задача с id=" + requestURI[2] + " удалена", 201);
        } else {
            sendNotFound(exchange, "Такой задачи нет");
        }
    }

    private void handleGetSubtasksByEpic(HttpExchange exchange) throws IOException {
        String[] requestURI = exchange.getRequestURI().getPath().split("/");
        String response = gson.toJson(taskManager.getSubtask(Integer.parseInt(taskManager.getEpic(Integer.parseInt(requestURI[2])))));
        if (response.equals("[]")) {
            sendNotFound(exchange, "Список задач пуст");
        } else {
            sendText(exchange, response, 200);
        }
    }
}