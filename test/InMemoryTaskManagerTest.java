package test;

import manager.*;
import tasks.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    private TaskManager manager = new InMemoryTaskManager();

    @Test
    public void testTaskOverlap() {
        Task task = new Task("Task 1", "Description", StatusTask.NEW,1,Duration.ofMinutes(30), LocalDateTime.now());
        Task task = new Task("Task 12", "Description2", StatusTask.NEW,2,Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(15));
        manager.addTask(task1);
        assertThrows(IllegalStateException.class, () -> manager.addTask(task2));
    }
}
