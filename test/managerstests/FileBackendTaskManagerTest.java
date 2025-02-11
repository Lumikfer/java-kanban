package test;

import managers.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import task.Epic;
import task.Subtask;
import task.Task;


import java.io.*;
import java.util.ArrayList;

public class FileBackendTaskManagerTest {
    String header = "id,type,name,status,description,epic";
    FileBackedTaskManager manager;
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    Epic epic3;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    File file = new File("test1.txt");

    @BeforeEach
    public void setUp() throws IOException {

        manager = new FileBackedTaskManager(file);
        task1 = new Task("TestName1", "TestDescription1");
        task2 = new Task("TestName4", "TestDescription4");
        epic1 = new Epic("TestName2", "TestDescription2");
        epic2 = new Epic("TestName5", "TestDescription5");
        epic3 = new Epic("TestName6", "TestDescription6");
        subtask2 = new Subtask("TestName8", "TestDescription8", 4);
        subtask3 = new Subtask("TestName9", "TestDescription9", 5);
    }
    @AfterEach
    public void tearDown() {
        file.deleteOnExit();
    }



    @Test
    public void loadManagerFromEmptyFile() {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertEquals(0, manager.getTasks().size());
        Assertions.assertEquals(0, manager.getSubtasks().size());
        Assertions.assertEquals(0, manager.getEpics().size());
        Assertions.assertEquals(0, manager.getHistory().size());
    }



    @Test
    public void loadTasksFromFileToManager() {
        task1 = new Task("TestName1", "TestDescription1", Status.NEW,1);
        task2 = new Task("TestName4", "TestDescription4",Status.NEW,2);
        epic1 = new Epic("TestName2", "TestDescription2",Status.NEW,3);
        subtask1 = new Subtask("TestName8", "TestDescription8", 4,Status.NEW,3);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(String.format("%s\n", header));
            writer.write(String.format("%s\n", task1.toString()));
            writer.write(String.format("%s\n", task2.toString()));
            writer.write(String.format("%s\n", epic1.toString()));
            writer.write(String.format("%s\n", subtask1.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertEquals(manager.getTasksById(1), task1);

    }

    @Test
    public void loadTasksFromFileToManagerAddOtherTasks() {
        task1 = new Task("TestName1", "TestDescription1");
        task2 = new Task("TestName4", "TestDescription4");
        epic1 = new Epic("TestName2", "TestDescription2");
        subtask1 = new Subtask("TestName8", "TestDescription8", 3);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(String.format("%s\n", header));
            writer.write(String.format("%s\n", task1.toString()));
            writer.write(String.format("%s\n", task2.toString()));
            writer.write(String.format("%s\n", epic1.toString()));
            writer.write(String.format("%s\n", subtask1.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
        Task task3 = new Task("TestName1", "TestDescription1",Status.NEW,5);
        Task task4 = new Task("TestName4", "TestDescription4",Status.NEW,6);
        Epic epic4 = new Epic("TestName4", "TestDescription4",Status.NEW,7);
        Subtask subtask4 = new Subtask("TestName8", "TestDescription8", 8,Status.NEW,7);
        manager.addTask(task3);
        manager.addTask(task4);
        manager.addEpic(epic4);
        manager.addSubtask(subtask4);
        Assertions.assertEquals(manager.getTasksById(3), task3);
        Assertions.assertEquals(manager.getTasksById(4), task4);
        Assertions.assertEquals(manager.getEpicsById(5), epic4);

    }

}
