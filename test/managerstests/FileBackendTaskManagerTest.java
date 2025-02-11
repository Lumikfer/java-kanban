package test;

import managers.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import task.Task;
import task.TaskParser;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class FileBackendTaskManagerTest {
    String header = "id,type,name,status,description,epic,duration,start time,end time";
    FileBackedTaskManager manager;
    File testTmpFile;
    Task task1;
    Task task2;
    Duration duration1 = Duration.ofMinutes(100);
    LocalDateTime startTime1 = LocalDateTime.of(LocalDate.of(2025, 2, 4),
            LocalTime.of(10, 0));
    LocalDateTime startTime2 = LocalDateTime.of(LocalDate.of(2025, 2, 5),
            LocalTime.of(10, 0));

    @BeforeEach
    public void setUp() throws IOException {

        testTmpFile = File.createTempFile("task", ".txt");
        manager = new FileBackedTaskManager(testTmpFile);
        task1 = new Task("TestName1", "TestDescription1", startTime1, duration1);
        task2 = new Task("TestName4", "TestDescription4", startTime2, duration1);

    }

    @Test
    public void loadManagerFromEmptyFile() {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testTmpFile);
        Assertions.assertEquals(0, manager.getTasks().size());
        Assertions.assertEquals(0, manager.getSubtasks().size());
        Assertions.assertEquals(0, manager.getEpics().size());
        Assertions.assertEquals(0, manager.getHistory().size());
    }

    @Test
    public void loadTasksFromFileToManager() {
        task1 = new Task("TestName1", "TestDescription1", Status.NEW, startTime1, duration1);
        task2 = new Task("TestName4", "TestDescription4", Status.NEW, startTime2, duration1);
        try (FileWriter writer = new FileWriter(testTmpFile)) {
            writer.write(String.format("%s\n", header));
            writer.write(String.format("%s\n", task1.toString()));
            writer.write(String.format("%s\n", task2.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testTmpFile);
        Assertions.assertEquals(manager.getTasksById(1), task1);
        Assertions.assertEquals(manager.getTasksById(2), task2);
    }

    @Test
    public void loadTasksFromFileToManagerAddOtherTasks() {
        task1 = new Task("TestName1", "TestDescription1", Status.NEW, startTime1, duration1);
        task2 = new Task("TestName4", "TestDescription4", Status.NEW, startTime2, duration1);

        try (FileWriter writer = new FileWriter(testTmpFile)) {
            writer.write(String.format("%s\n", header));
            writer.write(String.format("%s\n", task1.toString()));
            writer.write(String.format("%s\n", task2.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testTmpFile);
        Task task3 = new Task("TestName1", "TestDescription1", startTime1, duration1);
        Task task4 = new Task("TestName4", "TestDescription4", startTime2, duration1);
        manager.addTask(task3);
        manager.addTask(task4);
        Assertions.assertEquals(manager.getTasksById(5), task3);
        Assertions.assertEquals(manager.getTasksById(6), task4);
    }


}
