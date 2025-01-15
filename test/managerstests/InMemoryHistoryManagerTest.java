package managerstests;

import managers.Managers;
import status.Status;
import task.Epic;
import task.Subtask;
import task.Task;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryHistoryManagerTest {

    @Test
    void shouldAddTaskAndGetHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        historyManager.addTask(new Task("name1", "dsc1", Status.NEW));
        assertEquals(1, historyManager.getHistory().size());
        assertEquals("name1", historyManager.getHistory().getFirst().getName());
    }
}