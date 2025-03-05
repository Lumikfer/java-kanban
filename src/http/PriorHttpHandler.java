package http;

import managers.*;
import tasks.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.TreeSet;

public class PriorHttpHandler extends BaseHttpHandler implements HttpHandler {

    public  TaskManager taskManager;

    public PriorHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            String response = gson.toJson(taskManager.getPrioritizedTasks());
            if (response.isEmpty()) {
                sendNotFound(exchange, "список задач пуст");
            } else {
                TreeSet<Task> priorTask = taskManager.getPrioritizedTasks();
                sendText(exchange, gson.toJson(priorTask), 200);
            }
        } else {
            sendNotFound(exchange, "поинта не существует");
        }
    }
}
