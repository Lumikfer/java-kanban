package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import tasks.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler {

    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new AdapterForDuration())
            .registerTypeAdapter(LocalDateTime.class, new AdapterForLocalDateTime())
            .registerTypeAdapter(TaskType.class, new AdapterForTaskType())
            .create();

    protected void sendText(HttpExchange exchange, String responseText, int responseCode) throws IOException {
        byte[] responseBytes = responseText.getBytes(DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(responseCode, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange, String message) throws IOException {
        sendText(exchange, message, 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        sendText(exchange, "Новая задача пересекается с существующей", 406);
    }
}