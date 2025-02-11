import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import task.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {


        String fileName = "task.txt";

        InMemoryTaskManager manager = new InMemoryTaskManager();
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(fileName);
        Task task1 = fileBackedTaskManager.createTask("task1", "description task1", 160L, "12:00 26.11.2024");
        Task task2 = fileBackedTaskManager.createTask("task2", "description task2", 160L, "18:30 26.11.2024");
        Epic epicTask1 = fileBackedTaskManager.createEpicTask("epicTask1", "description epicTask1", 160L, "13:00 27.11.2024");
        Subtask subTask1 = fileBackedTaskManager.createSubTask(epicTask1, "subTask1", "description subTask1", 160L, "14:00 29.11.2024");
        if (subTask1 == null) {
            return;
        }

        Epic epicTask2 = manager.createEpicTask("epicTask2", "description epicTask2", 160L, "12:10 26.12.2024");
        if (epicTask2 == null) {
            return;
        }

        Subtask subTask2 = fileBackedTaskManager.createSubTask(epicTask1, "subTask2", "description subTask2", 160L, "12:20 30.01.2024");
        if (subTask2 == null) {
            return;

        }
        Subtask subTask3 = manager.createSubTask(epicTask2, "subTask3", "description subTask3", 160L, "15:50 30.01.2024");
        if (subTask3 == null) {
            return;
        }

        printAllTasks(manager);
        task2 = manager.updateTask(task2, "task3", "description task3", 160L, "12:00 26.11.2024");
        epicTask1 = manager.updateEpicTask(epicTask1, "epicTask3", "description epicTask3", 160L, "12:00 26.03.2024");
        subTask3 = manager.updateSubTask(subTask3, "subTask4", "description subTask4", 160L, "12:00 26.04.2024");
        printAllTasks(manager);
        if (task2 != manager.getTaskById(task2.getId())) {
            return;
        }

        if (epicTask1 != manager.getEpicTaskById(epicTask1.getId())) {
            return;
        }

        if (subTask3 != manager.getSubTaskById(subTask3.getId())) {
            return;
        }

        manager.moveTaskToProgress(task1.getId());
        manager.moveSubTaskToProgress(subTask1.getId());
        manager.moveSubTaskToProgress(subTask3.getId());
        printAllTasks(manager);
        manager.moveTaskToDone(task1.getId());
        manager.moveSubTaskToDone(subTask1.getId());
        manager.moveSubTaskToDone(subTask3.getId());
        printAllTasks(manager);
        task2 = manager.deleteTaskById(task2.getId());
        epicTask1 = manager.deleteEpicTaskById(epicTask1.getId());
        printAllTasks(manager);
        manager.clearListTasks();
        printAllTasks(manager);
        manager.clearListEpicTasks();
        printAllTasks(manager);
        FileBackedTaskManager.loadFromFile(fileName);
        printAllTasks(manager);
        printHistoryTask(manager);
    }

    static void printHistoryTask(InMemoryTaskManager manager) {
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

    static void printTasks(InMemoryTaskManager manager) {
        for (Task task : manager.getListTasks()) {
            System.out.println(task);
        }
    }

    static void printEpicTasks(InMemoryTaskManager manager) {
        for (Task task : manager.getListEpicTasks()) {
            System.out.println(task);
        }
    }

    static void printSubTasks(InMemoryTaskManager manager) {
        for (Task task : manager.getListSubTasks()) {
            System.out.println(task);
        }
    }

    static void printAllTasks(InMemoryTaskManager manager) {
        printTasks(manager);
        printEpicTasks(manager);
        printSubTasks(manager);
    }
}
