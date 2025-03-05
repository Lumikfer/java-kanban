package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.*;
import tasks.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TaskHttpHandler extends BaseHttpHandler implements HttpHandler {
    public TaskManager taskManager;

    public TaskHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        EndPoints endpoint = points(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS:
                GetTask(exchange);
                break;
            case GET_TASK_ID:
                GetTaskId(exchange);
                break;
            case POST_TASK:
                PostTask(exchange);
                break;
            case DELETE_TASK:
                DelTask(exchange);
                break;
            default:
                sendNotFound(exchange, "поинта не сущетсвует");
        }
    }

    private EndPoints points(String requestPath, String requestMethod) {
        String[] e = requestPath.split("/");
        if (e.length == 2 && e[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                return EndPoints.GET_TASKS;
            }
            if (requestMethod.equals("POST")) {
                return EndPoints.POST_TASK;
            }
        }
        if (e.length == 3 && e[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                return EndPoints.GET_TASK_ID;
            }
            if (requestMethod.equals("DELETE")) {
                return EndPoints.DELETE_TASK;
            }
        }
        return EndPoints.UNKNOWN;
    }

    private void DelTask(HttpExchange exchange) throws IOException {
        String[] e = exchange.getRequestURI().getPath().split("/");
        if (taskManager.getTask(Integer.parseInt(e[2])) != null) {
            taskManager.deleteTask(Integer.parseInt(e[2]));
            sendText(exchange, "task удалена", 201);
        } else {
            sendNotFound(exchange, "такой задачи нет");
        }

    }

    private void GetTask(HttpExchange exchange) throws IOException {

        String res = gson.toJson(taskManager.getTasks());
        if (res.isEmpty()) {
            sendNotFound(exchange, "список пустой");
        } else {
            sendText(exchange, res, 200);
        }

    }

    private void GetTaskId(HttpExchange exchange) throws IOException {
        String[] e = exchange.getRequestURI().getPath().split("/");
        String res = gson.toJson(taskManager.getTask(Integer.parseInt(e[2])));
        if (res.isEmpty()) {
            sendNotFound(exchange, "такой задачи не суущетсвует");
        } else {
            sendText(exchange, res, 200);
        }
    }

    private void PostTask(HttpExchange exchange) throws IOException {

        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        Task task = gson.fromJson(requestBody, Task.class);

        if (taskManager.getTask(task.getId()) == null) {
            taskManager.addTask(task);
            sendText(exchange, "задача добавлена", 200);
        }
        if (taskManager.isIntersectionTaskTime(task)) {
            sendHasInteractions(exchange);
        } else {
            taskManager.updateTask(task);
            sendText(exchange, "задача обновлена", 201);
        }

    }

}
