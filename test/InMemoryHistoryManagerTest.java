package solid;

import manager.*;
import tasks.*;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;

public class InMemoryHistoryManagerTest {

    @Test
    public void testAddToHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Task 1", "Description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        historyManager.add(task);
        assertEquals(List.of(task), historyManager.getHistory());
    }

    @Test
    public void testRemoveFromHistory() {
        TaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task("Task 1", "Description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusHours(1));
        manager.addTask(task1);
        manager.addTask(task2);
        manager.getTaskByID(task1.getId());
        manager.getTaskByID(task2.getId());
        manager.removeTaskByID(task1.getId());
        assertEquals(List.of(task2), manager.getHistory());
    }

    @Test
    public void testEmptyHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    public void testDuplicateHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Task 1", "Description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        historyManager.add(task);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size());
    }
}