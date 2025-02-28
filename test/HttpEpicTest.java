import http.HttpTaskServer;
import http.json.JsonTaskBuilder;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import util.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class HttpEpicTest {

    TaskManager manager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(manager);
    JsonTaskBuilder json = server.getJsonTaskBuilder();
    URI epicUrl = URI.create("http://localhost:8080/epics");

    public HttpEpicTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test
    public void getEpics() throws IOException, InterruptedException {
        manager.addEpic(new Epic("test_epic", "descr"));
        manager.addEpic(new Epic("test_epic2", "descr2"));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(epicUrl).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        String epics = json.toJson(manager.getEpics());
        Assertions.assertEquals(epics, response.body());
    }

    @Test
    public void getEpic() throws IOException, InterruptedException {
        manager.addEpic(new Epic("test_epic", "descr"));
        Epic epic = manager.getEpic(1);
        epicUrl = URI.create("http://localhost:8080/epics/" + epic.getId());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(epicUrl).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        Assertions.assertEquals(json.toJson(epic), response.body());


    }

    @Test
    public void deleteEpic() throws IOException, InterruptedException {
        manager.addEpic(new Epic("test_epic", "descr"));
        Epic epic = manager.getEpic(1);
        epicUrl = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(epicUrl).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        ArrayList<Epic> epics = manager.getEpics();

        Assertions.assertTrue(epics.isEmpty());
    }

    @Test
    public void getEpicNotFound() throws IOException, InterruptedException {
        manager.addEpic(new Epic("test_epic", "descr"));
        epicUrl = URI.create("http://localhost:8080/epics/3");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(epicUrl).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals("Эпик с указанным id не найден", response.body());

    }

    @Test
    public void deleteNotFound() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        epicUrl = URI.create("http://localhost:8080/epics/3");
        HttpRequest request = HttpRequest.newBuilder().uri(epicUrl).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals("Эпик не найден!", response.body());
    }

}
