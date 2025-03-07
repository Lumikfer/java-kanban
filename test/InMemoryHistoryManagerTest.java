package test;

import managers.HistoryManager;
import managers.InMemoryHistoryManager;
import tasks.StatusTask;
import tasks.Task;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    @Test
    public void testAddToHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Task 1", "Description", StatusTask.NEW, 1, Duration.ofMinutes(30), LocalDateTime.now());
        historyManager.add(task);
        assertEquals(List.of(task), historyManager.getHistory());
    }

    @Test
    public void testRemoveFromHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Description", StatusTask.NEW, 1, Duration.ofMinutes(30), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description", StatusTask.NEW, 2, Duration.ofMinutes(30), LocalDateTime.now());

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());

        assertEquals(List.of(task2), historyManager.getHistory());
    }

    @Test
    public void testEmptyHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    public void testDuplicateHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Task 1", "Description", StatusTask.NEW, 1, Duration.ofMinutes(30), LocalDateTime.now());
        historyManager.add(task);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size());
    }
}