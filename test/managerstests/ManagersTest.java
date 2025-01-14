package managerstests;

import managers.Managers;
import org.junit.jupiter.api.Test;
import managers.TaskManager;
import managers.InMemoryHistoryManager;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {


    InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    void setUp() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    @Test
    void shouldGetDefault() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Assertions.assertEquals(inMemoryTaskManager.getClass(), Managers.getDefault().getClass());
    }

    @Test
    void shouldGetHistory() {
        assertEquals(inMemoryHistoryManager.getClass(), Managers.getDefaultHistory().getClass());
    }
}