package taskstests;

import status.Status;
import task.Epic;
import task.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    Subtask subtask;

    @BeforeEach
    void setUp() {
        subtask = new Subtask("name", "description", Status.NEW, 1);
    }

    @Test
    void shouldGetEpicId() {
        Assertions.assertEquals(1, subtask.getEpicId());
    }
}