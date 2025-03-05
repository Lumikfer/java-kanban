package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import java.io.IOException;
import tasks.Task;
import java.util.List;

public class Histhttphandler extends BaseHttpHandler implements HttpHandler {

    public TaskManager taskManager;

    public Histhttphandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            String response = gson.toJson(taskManager.getPrioritizedTasks());
            if (response.isEmpty()) {
                sendNotFound(exchange, "список задач пуст");
            } else {
                List<Task> histTask = taskManager.getHistory();
                sendText(exchange, gson.toJson(histTask), 200);
            }
        } else {
            sendNotFound(exchange, "поинта не существует");
        }
    }
}
