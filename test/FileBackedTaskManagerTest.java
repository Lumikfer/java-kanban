package solid;

import manager.*;
import tasks.*;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    private File file;

    protected FileBackedTaskManager createTaskManager() {
        try {
            file = Files.createTempFile("tasks", ".csv").toFile();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания временного файла", e);
        }
        return new FileBackedTaskManager(file);
    }

    private final FileBackedTaskManager manager = createTaskManager();

    @Test
    public void testSaveAndLoad() {
        Task task = new Task("Task 1", "Description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        manager.addTask(task);
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(task, loadedManager.getTaskByID(task.getId()));
    }
}