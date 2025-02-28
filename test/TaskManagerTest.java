package solid;

import manager.*;
import tasks.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


public class TaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    void beforeEach() {

        manager = Managers.getDefault();

    }

    @Test
    void testAddTask() {
        Task task = new Task("Task 1", "Description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        manager.addTask(task);
        assertEquals(task, manager.getTaskByID(task.getId()));
    }

    @Test
    void testAddEpic() {
        Epic epic = new Epic("Epic 1", "Description");
        manager.addEpic(epic);
        assertEquals(epic, manager.getEpicByID(epic.getId()));
    }

    @Test
    void testAddSubtask() {
        Epic epic = new Epic("Epic 1", "Description");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description", TaskStatus.NEW, epic.getId(), Duration.ofMinutes(30), LocalDateTime.now());
        manager.addSubtask(subtask);
        assertEquals(subtask, manager.getSubtaskByID(subtask.getId()));
    }

    @Test
    void testUpdateTask() {
        Task task = new Task("Task 1", "Description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        manager.addTask(task);
        task.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(task);
        assertEquals(TaskStatus.IN_PROGRESS, manager.getTaskByID(task.getId()).getStatus());
    }

    @Test
    void testUpdateEpic() {
        Epic epic = new Epic("Epic 1", "Description");
        manager.addEpic(epic);
        epic.setName("Updated Epic");
        manager.updateEpic(epic);
        assertEquals("Updated Epic", manager.getEpicByID(epic.getId()).getName());
    }

    @Test
    void testUpdateSubtask() {
        Epic epic = new Epic("Epic 1", "Description");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description", TaskStatus.NEW, epic.getId(), Duration.ofMinutes(30), LocalDateTime.now());
        manager.addSubtask(subtask);
        subtask.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask);
        assertEquals(TaskStatus.DONE, manager.getSubtaskByID(subtask.getId()).getStatus());
    }

    @Test
    void testRemoveTaskByID() {
        Task task = new Task("Task 1", "Description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        manager.addTask(task);
        manager.removeTaskByID(task.getId());
        assertNull(manager.getTaskByID(task.getId()));
    }

    @Test
    void testRemoveEpicByID() {
        Epic epic = new Epic("Epic 1", "Description");
        manager.addEpic(epic);
        manager.removeEpicByID(epic.getId());
        assertNull(manager.getEpicByID(epic.getId()));
    }

    @Test
    void testRemoveSubtaskByID() {
        Epic epic = new Epic("Epic 1", "Description");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description", TaskStatus.NEW, epic.getId(), Duration.ofMinutes(30), LocalDateTime.now());
        manager.addSubtask(subtask);
        manager.removeSubtaskByID(subtask.getId());
        assertNull(manager.getSubtaskByID(subtask.getId()));
    }

    @Test
    void testGetPrioritizedTasks() {
        Task task1 = new Task("Task 1", "Description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusHours(1));
        manager.addTask(task1);
        manager.addTask(task2);
        List<Task> prioritizedTasks = manager.getPrioritizedTasks();
        assertEquals(task1, prioritizedTasks.get(0));
        assertEquals(task2, prioritizedTasks.get(1));
    }

    @Test
    void testEpicStatusCalculation() {
        Epic epic = new Epic("Epic 1", "Description");
        manager.addEpic(epic);

        // Все подзадачи со статусом NEW
        Subtask subtask1 = new Subtask("Subtask 1", "Description", TaskStatus.NEW, epic.getId(), Duration.ofMinutes(30), LocalDateTime.now());
        manager.addSubtask(subtask1);
        assertEquals(TaskStatus.NEW, epic.getStatus());

        // Все подзадачи со статусом DONE
        subtask1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask1);
        assertEquals(TaskStatus.DONE, epic.getStatus());

        // Подзадачи со статусами NEW и DONE
        Subtask subtask2 = new Subtask("Subtask 2", "Description", TaskStatus.NEW, epic.getId(), Duration.ofMinutes(30), LocalDateTime.now().plusHours(1));
        manager.addSubtask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());

        // Подзадачи со статусом IN_PROGRESS
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }
}