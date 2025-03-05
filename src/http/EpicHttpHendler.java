package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EpicHttpHendler extends BaseHttpHandler implements HttpHandler {

    public TaskManager taskManager;

    public EpicHttpHendler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        EndPoints endpoint = points(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS:
                GetEpics(exchange);
                break;
            case GET_TASK_ID:
                GetEpicId(exchange);
                break;
            case POST_TASK:
                PostEpic(exchange);
                break;
            case DELETE_TASK:
                DelEpic(exchange);
                break;
            case GET_SUBTASKS_BY_EPIC:
                GetSubByEpic(exchange);
                break;
            default:
                sendNotFound(exchange, "поинта не сущетсвует");
        }
    }

    private EndPoints points(String requestPath, String requestMethod) {
        String[] e = requestPath.split("/");
        if (e.length == 2 && e[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return EndPoints.GET_TASKS;
            }
            if (requestMethod.equals("POST")) {
                return EndPoints.POST_TASK;
            }
        }
        if (e.length == 3 && e[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return EndPoints.GET_TASK_ID;
            }
            if (requestMethod.equals("DELETE")) {
                return EndPoints.DELETE_TASK;
            }
        }
        if(e.length > 3 && e[1].equals("epics")) {
         if(requestMethod.equals("GET")){
             return EndPoints.GET_SUBTASKS_BY_EPIC;
         }
        }
        return EndPoints.UNKNOWN;
    }

    private void postEpic(HttpExchange exchange) throws  IOException {

        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8 );

        Epic task = gson.fromJson(requestBody, Epic.class);
        if(taskManager.isIntersectionTaskTime(task)) {
            sendHasInteractions(exchange);
        }
        if(taskManager.getTask(task.getId()) == null) {
            taskManager.addEpic(task);
            sendText(exchange,"epic добавлен",200);
        }
        else {
            taskManager.updateEpic(task);
            sendText(exchange,"epic обновлен",201);
        }

    }

    private  void getEpics(HttpExchange exchange) throws IOException {

        String res = gson.toJson(taskManager.getEpics());
        if(res.isEmpty()) {
            sendNotFound(exchange,"список epics пустой");
        } else {
            sendText(exchange,res,200);
        }
    }

    private void getEpicId(HttpExchange exchange) throws IOException {
        String[] e = exchange.getRequestURI().getPath().split("/");
        String res = gson.toJson(taskManager.getEpic(Integer.parseInt(e[2])));
        if(res.isEmpty()) {
            sendNotFound(exchange,"такого epic не суущетсвует");
        }else{
            sendText(exchange,res,200);
        }
    }

    private void delEpic(HttpExchange exchange) throws IOException {
        String[] e = exchange.getRequestURI().getPath().split("/");
        String res = gson.toJson(taskManager.getEpic(Integer.parseInt(e[2])));
        if(res.isEmpty()) {
            sendNotFound(exchange,"такого epic не суущетсвует");
        }else{
            taskManager.deleteSubtask(Integer.parseInt(e[2]));
            sendText(exchange,"epic удален",200);
        }
    }

    private void getSubByEpic(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8 );
        Epic task = gson.fromJson(requestBody, Epic.class);
        String[] e = exchange.getRequestURI().getPath().split("/");
        String res = gson.toJson(taskManager.getEpicSubtasks(task.getId()));
        if(res.isEmpty()) {
            sendNotFound(exchange,"такого epic не суущетсвует");
        }else {
            sendText(exchange,res,200);
        }
    }
}
