package solid;

import manager.*;
import tasks.*;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class HistoryManagerTest {

    @Test
    void newHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Менеджер не проинициализирован");
    }

    @Test
    void add() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Task1", "description", TaskStatus.NEW);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void remove() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Task1", "description", TaskStatus.NEW);
        historyManager.remove(task.getId());
        final List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "История пустая.");
    }

}