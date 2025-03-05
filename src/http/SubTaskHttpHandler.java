package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.*;
import  tasks.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SubTaskHttpHandler extends  BaseHttpHandler implements HttpHandler {
    public  TaskManager taskManager;
    public SubTaskHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        EndPoints endpoint = points(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS:
                GetSubTask(exchange);
                break;

            case GET_TASK_ID:
                GetSubTaskId(exchange);
                break;

            case POST_TASK:
                PostSubTask(exchange);
                break;

            case DELETE_TASK:
                DelSubTask(exchange);
                break;

            default:
                sendNotFound(exchange, "поинта не сущетсвует");
        }
    }
    private EndPoints points (String requestPath, String requestMethod) {
        String[] e = requestPath.split("/");
        if (e.length == 2 && e[1].equals("subtasks")) {
            if (requestMethod.equals("GET")) {
                return EndPoints.GET_TASKS;
            }
            if (requestMethod.equals("POST")) {
                return EndPoints.POST_TASK;
            }
        }
        if (e.length == 3 && e[1].equals("subtasks")) {
            if (requestMethod.equals("GET")) {
                return EndPoints.GET_TASK_ID;
            }
            if (requestMethod.equals("DELETE")) {
                return EndPoints.DELETE_TASK;
            }
        }
        return EndPoints.UNKNOWN;
    }

    private void PostSubTask(HttpExchange exchange) throws  IOException {

        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8 );

        Subtask task = gson.fromJson(requestBody, Subtask.class);
        if(taskManager.isIntersectionTaskTime(task)) {
            sendHasInteractions(exchange);
        }
        if(taskManager.getTask(task.getId()) == null) {
            taskManager.addSubtask(task);
            sendText(exchange,"задача добавлена",200);
        }
        else {
            taskManager.updateSubtask(task);
            sendText(exchange,"задача обновлена",201);
        }

    }

    private  void GetSubTask(HttpExchange exchange) throws IOException {

        String res = gson.toJson(taskManager.getSubtasks());
        if(res.isEmpty()) {
            sendNotFound(exchange,"список пустой");
        } else {
            sendText(exchange,res,200);
        }
    }

    private void GetSubTaskId(HttpExchange exchange) throws IOException {
        String[] e = exchange.getRequestURI().getPath().split("/");
        String res = gson.toJson(taskManager.getSubtask(Integer.parseInt(e[2])));
        if(res.isEmpty()) {
            sendNotFound(exchange,"такой задачи не суущетсвует");
        }else{
            sendText(exchange,res,200);
        }
    }

    private void DelSubTask(HttpExchange exchange) throws IOException {
        String[] e = exchange.getRequestURI().getPath().split("/");
        String res = gson.toJson(taskManager.getSubtask(Integer.parseInt(e[2])));
        if(res.isEmpty()) {
            sendNotFound(exchange,"такой задачи не суущетсвует");
        }else{
            taskManager.deleteSubtask(Integer.parseInt(e[2]));
            sendText(exchange,"subtask удален",200);
        }
    }


}
