package test;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.*;
import task.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManagerTest {
    InMemoryTaskManager manager;
    Task task1;
    Task task2;
    Task task3;
    Task task4;
    Epic epic1;
    Epic epic2;
    Epic epic3;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    Subtask subtask4;
    Subtask subtask5;
    Subtask subtask6;
    Duration duration1 = Duration.ofMinutes(100);
    LocalDateTime startTime1 = LocalDateTime.of(LocalDate.of(2025, 2, 4),
            LocalTime.of(10, 0));
    LocalDateTime startTime2 = LocalDateTime.of(LocalDate.of(2025, 2, 5),
            LocalTime.of(10, 0));
    LocalDateTime startTime3 = LocalDateTime.of(LocalDate.of(2025, 2, 6),
            LocalTime.of(10, 0));
    LocalDateTime startTime4 = LocalDateTime.of(LocalDate.of(2025, 2, 7),
            LocalTime.of(10, 0));
    LocalDateTime startTime5 = LocalDateTime.of(LocalDate.of(2025, 2, 8),
            LocalTime.of(10, 0));

    @BeforeEach
    public void setUp() {
        manager = new InMemoryTaskManager();
        task1 = new Task("TestName1", "TestDescription1", startTime1, duration1);
        task2 = new Task("TestName4", "TestDescription4", startTime2, duration1);

    }

    @Test
    public void createIdForTask() {
        manager.addTask(task1);
        manager.addEpic(epic1);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), startTime3, duration1);
        manager.addSubtask(subtask1);
        Assertions.assertEquals(1, task1.getId());
        Assertions.assertEquals(2, epic1.getId());
        Assertions.assertEquals(3, subtask1.getId());
    }

    @Test
    public void addTasks() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), startTime1, duration1);
        manager.addSubtask(subtask1);
        Assertions.assertEquals(2, manager.getTasks().size());
        Assertions.assertEquals(2, manager.getEpics().size());
        Assertions.assertEquals(1, manager.getSubtasks().size());

    }

    @Test
    public void addTaskWithTimeIntersection() {
        manager.addTask(task1);
        task2 = new Task("TestName2", "TestDescription2", LocalDateTime.of(LocalDate.of(2025, 2, 4),
                LocalTime.of(10, 5)), duration1);
        manager.addTask(task2);
        Assertions.assertEquals(1, manager.getTasks().size());
        task3 = new Task("TestName3", "TestDescription3", startTime1, duration1);
        Assertions.assertEquals(1, manager.getTasks().size());
        task4 = new Task("TestName4", "TestDescription4", startTime2, duration1);
        manager.addTask(task4);
        Assertions.assertEquals(2, manager.getTasks().size());
    }

    @Test
    public void addSubtaskWithTimeIntersection() {
        manager.addEpic(epic1);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), startTime1, duration1);
        manager.addSubtask(subtask1);
        subtask2 = new Subtask("TestName4", "TestDescription4", epic1.getId(), LocalDateTime.of(LocalDate.of(2025, 2, 4),
                LocalTime.of(10, 5)), duration1);
        manager.addSubtask(subtask2);
        Assertions.assertEquals(1, manager.getSubtasks().size());
        subtask3 = new Subtask("TestName3", "TestDescription3", epic1.getId(), startTime1, duration1);
        Assertions.assertEquals(1, manager.getSubtasks().size());
        subtask4 = new Subtask("TestName4", "TestDescription4", epic1.getId(), startTime2, duration1);
        manager.addSubtask(subtask4);
        Assertions.assertEquals(2, manager.getSubtasks().size());
    }

    @Test
    public void addSubtaskInEpic() {
        manager.addEpic(epic1);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), startTime1, duration1);
        subtask2 = new Subtask("TestName7", "TestDescription7", epic1.getId(), startTime1, duration1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        Assertions.assertEquals(1, subtask1.getEpicId());
        Assertions.assertEquals(1, subtask2.getEpicId());


    }


    @Test
    public void getPrioritizedTasks() {
        task1 = new Task("TestName1", "TestDescription1", startTime1, duration1);
        task2 = new Task("TestName2", "TestDescription2", startTime2, duration1);
        task3 = new Task("TestName3", "TestDescription3", startTime3, duration1);
        manager.addTask(task2);
        manager.addTask(task1);
        manager.addTask(task3);
        ArrayList<Task> prioritizedTasks = new ArrayList<>(manager.getPrioritizedTasks());
        ArrayList<Task> expectedPrioritizedTasks = new ArrayList<>();
        expectedPrioritizedTasks.add(task1);
        expectedPrioritizedTasks.add(task2);
        expectedPrioritizedTasks.add(task3);
        Assertions.assertEquals(expectedPrioritizedTasks, prioritizedTasks);
    }

    @Test
    public void clearTasks() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), startTime1, duration1);
        subtask2 = new Subtask("TestName7", "TestDescription7", epic2.getId(), startTime1, duration1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.removeAllTasks();
        Assertions.assertEquals(0, manager.getTasks().size());
        manager.removeAllSubtasks();
        Assertions.assertEquals(0, manager.getSubtasks().size());
        manager.removeAllEpics();
        Assertions.assertEquals(0, manager.getEpics().size());
    }

    @Test
    public void clearEpicsWithSubtasks() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        subtask1 = new Subtask("TestName3", "TestDescription3", epic1.getId(), startTime1, duration1);
        subtask2 = new Subtask("TestName7", "TestDescription7", epic2.getId(), startTime1, duration1);
        subtask3 = new Subtask("TestName8", "TestDescription8", epic2.getId(), startTime1, duration1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.removeAllEpics();
        Assertions.assertEquals(0, manager.getEpics().size());
        Assertions.assertEquals(0, manager.getSubtasks().size());
    }


    @Test
    public void getHistoryTasks() {
        task3 = new Task("TestName9", "TestDescription9", startTime3, duration1);
        task4 = new Task("TestName10", "TestDescription10", startTime4, duration1);

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        subtask2 = new Subtask("TestName2", "TestDescription2", epic1.getId(), startTime1, duration1);
        subtask3 = new Subtask("TestName3", "TestDescription3", epic1.getId(), startTime2, duration1);
        subtask4 = new Subtask("TestName4", "TestDescription4", epic2.getId(), startTime3, duration1);
        subtask5 = new Subtask("TestName5", "TestDescription5", epic2.getId(), startTime4, duration1);
        subtask6 = new Subtask("TestName6", "TestDescription6", epic3.getId(), startTime5, duration1);

        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        manager.addSubtask(subtask5);
        manager.addSubtask(subtask6);

        Assertions.assertTrue(manager.getHistory().isEmpty());

        manager.getTasksById(1);
        manager.getEpicsById(5);
        manager.getSubtasksById(8);
        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(task1);
        expectedHistory.add(epic1);
        expectedHistory.add(subtask2);
        Assertions.assertEquals(3, manager.getHistory().size());
        Assertions.assertEquals(expectedHistory, manager.getHistory());

        manager.getTasksById(2);
        manager.getTasksById(3);
        manager.getEpicsById(6);
        manager.getSubtasksById(10);
        manager.getEpicsById(7);
        manager.getSubtasksById(9);
        manager.getSubtasksById(12);
        manager.getSubtasksById(11);
        Assertions.assertEquals(11, manager.getHistory().size());
        Assertions.assertEquals(task1, manager.getHistory().getFirst());
        Assertions.assertEquals(subtask5, manager.getHistory().getLast());
    }

    @Test
    public void deleteTaskdeleteTaskInHistory() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        subtask2 = new Subtask("TestName2", "TestDescription2", epic1.getId(), startTime1, duration1);
        subtask3 = new Subtask("TestName3", "TestDescription3", epic1.getId(), startTime1, duration1);
        subtask4 = new Subtask("TestName4", "TestDescription4", epic2.getId(), startTime1, duration1);
        subtask5 = new Subtask("TestName5", "TestDescription5", epic2.getId(), startTime1, duration1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        manager.addSubtask(subtask5);
        ArrayList<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(task1);
        expectedHistory.add(subtask4);
        expectedHistory.add(subtask5);

        manager.getTasksById(1);
        manager.getTasksById(2);
        manager.getEpicsById(3);
        manager.getSubtasksById(5);
        manager.getSubtasksById(6);
        manager.getSubtasksById(7);
        manager.getSubtasksById(8);

        Assertions.assertEquals(7, manager.getHistory().size());

        manager.removeEpicById(3);
        manager.removeTaskById(2);
        Assertions.assertEquals(3, manager.getHistory().size());
        Assertions.assertEquals(expectedHistory, manager.getHistory());
    }

    @Test
    public void clearTasksAndHistory() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        subtask2 = new Subtask("TestName2", "TestDescription2", epic1.getId(), startTime1, duration1);
        subtask3 = new Subtask("TestName3", "TestDescription3", epic1.getId(), startTime1, duration1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.getTasksById(1);
        manager.getTasksById(2);
        manager.getEpicsById(3);
        manager.getSubtasksById(4);
        manager.getSubtasksById(5);
        ArrayList<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(epic1);
        expectedHistory.add(subtask2);
        expectedHistory.add(subtask3);
        manager.removeAllTasks();
        Assertions.assertEquals(3, manager.getHistory().size());
        Assertions.assertEquals(expectedHistory, manager.getHistory());
        manager.removeAllEpics();
        expectedHistory.remove(epic1);
        expectedHistory.remove(subtask2);
        expectedHistory.remove(subtask3);
        Assertions.assertTrue(manager.getHistory().isEmpty());
        Assertions.assertEquals(expectedHistory, manager.getHistory());
        manager.addEpic(epic3);
        subtask3 = new Subtask("TestName4", "TestDescription4", epic3.getId(), startTime1, duration1);
        subtask4 = new Subtask("TestName5", "TestDescription5", epic3.getId(), startTime1, duration1);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        manager.getEpicsById(6);
        manager.getSubtasksById(7);
        manager.getSubtasksById(8);
        expectedHistory.add(epic3);
        manager.removeAllSubtasks();
        Assertions.assertEquals(1, manager.getHistory().size());
        Assertions.assertEquals(expectedHistory, manager.getHistory());
    }
}