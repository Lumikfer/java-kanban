package test;

import managers.*;
import tasks.*;
import statuses.*;
import util.*;

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
        Task task = new Task("Task 1", "Description", StatusTask.NEW, 1, Duration.ofMinutes(30), LocalDateTime.now());
        manager.addTask(task);
        assertEquals(task, manager.getTask(task.getId()));
    }

    @Test
    void testAddEpic() {
        Epic epic = new Epic("Epic 1", "Description", StatusTask.NEW, 5, LocalDateTime.now());
        manager.addEpic(epic);
        assertEquals(epic, manager.getEpic(epic.getId()));
    }

    @Test
    void testAddSubtask() {
        Epic epic = new Epic("Epic 1", "Description", StatusTask.NEW, 5, LocalDateTime.now());
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description", StatusTask.NEW, epic.getId(), 6, LocalDateTime.now());
        manager.addSubtask(subtask);
        assertEquals(subtask, manager.getSubtask(subtask.getId()));
    }

    @Test
    void testUpdateTask() {
        Task task = new Task("Task 1", "Description", StatusTask.NEW, 1, Duration.ofMinutes(30), LocalDateTime.now());
        manager.addTask(task);
        task.setStatus(StatusTask.IN_PROGRESS);
        manager.updateTask(task);
        assertEquals(StatusTask.IN_PROGRESS, manager.getTask(task.getId()).getStatus());
    }

    @Test
    void testUpdateEpic() {
        Epic epic = new Epic("Epic 1", "Description", StatusTask.NEW, 5, LocalDateTime.now());
        manager.addEpic(epic);
        epic.setName("Updated Epic");
        manager.updateEpic(epic);
        assertEquals("Updated Epic", manager.getEpic(epic.getId()).getName());
    }

    @Test
    void testUpdateSubtask() {
        Epic epic = new Epic("Epic 1", "Description", StatusTask.NEW, 5, LocalDateTime.now());
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description", StatusTask.NEW, epic.getId(), 6, LocalDateTime.now());
        manager.addSubtask(subtask);
        subtask.setStatus(StatusTask.DONE);
        manager.updateSubtask(subtask);
        assertEquals(StatusTask.DONE, manager.getSubtask(subtask.getId()).getStatus());
    }

    @Test
    void testRemoveTaskByID() {
        Task task = new Task("Task 1", "Description", StatusTask.NEW, 1, Duration.ofMinutes(30), LocalDateTime.now());
        manager.addTask(task);
        manager.deleteTask(task.getId());
        assertNull(manager.getTask(task.getId()));
    }

    @Test
    void testRemoveEpicByID() {
        Epic epic = new Epic("Epic 1", "Description", StatusTask.NEW, 5, LocalDateTime.now());
        manager.addEpic(epic);
        manager.deleteEpic(epic.getId());
        assertNull(manager.getEpic(epic.getId()));
    }
}