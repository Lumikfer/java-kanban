package managerstests;

import managers.Managers;
import status.Status;
import task.Epic;
import task.Subtask;
import task.Task;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();

        Task task1 = new Task("name1", "dsc1", Status.NEW);
        Task task2 = new Task("name2", "dsc2", Status.NEW);
        Task task3 = new Task("name3", "dsc3", Status.NEW);

        Epic epic1 = new Epic("epic1", "dsc1");
        Epic epic2 = new Epic("epic2", "dsc2");

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask subtask1 = new Subtask("sbt1", "dsc1", Status.NEW, 4);
        Subtask subtask2 = new Subtask("sbt2", "dsc2", Status.NEW, 4);

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
    }

    @Test
    void shouldGetTasks() {
        Task task1 = new Task("name1", "dsc1", Status.NEW);
        task1.setId(1);
        Task task2 = new Task("name2", "dsc2", Status.NEW);
        task2.setId(2);
        Task task3 = new Task("name3", "dsc3", Status.NEW);
        task3.setId(3);

        ArrayList<Task> list = new ArrayList<>();
        list.add(task1);
        list.add(task2);
        list.add(task3);

        assertEquals(list, taskManager.getTasks());
    }

    @Test
    void shouldGetEpics() {
        Epic epic1 = new Epic("epic1", "dsc1");
        epic1.setId(4);
        Epic epic2 = new Epic("epic2", "dsc2");
        epic2.setId(5);

        ArrayList<Epic> list = new ArrayList<>();
        list.add(epic1);
        list.add(epic2);

        assertEquals(list, taskManager.getEpics());
    }

    @Test
    void shouldGetSubtasks() {
        Subtask subtask1 = new Subtask("sbt1", "dsc1", Status.NEW, 4);
        subtask1.setId(6);
        Subtask subtask2 = new Subtask("sbt2", "dsc2", Status.NEW, 4);
        subtask2.setId(7);

        ArrayList<Subtask> list = new ArrayList<>();
        list.add(subtask1);
        list.add(subtask2);

        assertEquals(list, taskManager.getSubtasks());
    }

    @Test
    void shouldRmvAllTypesOfTasks() {
        taskManager.rmvAllTypesOfTasks();

        ArrayList<Task> list1 = new ArrayList<>();
        ArrayList<Epic> list2 = new ArrayList<>();
        ArrayList<Subtask> list3 = new ArrayList<>();

        assertEquals(list1, taskManager.getTasks());
        assertEquals(list2, taskManager.getEpics());
        assertEquals(list3, taskManager.getSubtasks());
    }

    @Test
    void shouldRmvAllTasks() {
        ArrayList<Task> list1 = new ArrayList<>();
        taskManager.rmvAllTasks();
        assertEquals(list1, taskManager.getTasks());
    }

    @Test
    void shouldRmvAllEpics() {
        ArrayList<Epic> list2 = new ArrayList<>();
        taskManager.rmvAllEpics();
        assertEquals(list2, taskManager.getEpics());
    }

    @Test
    void shouldRmvAllSubtasks() {
        ArrayList<Subtask> list3 = new ArrayList<>();
        taskManager.rmvAllSubtasks();
        assertEquals(list3, taskManager.getSubtasks());
    }

    @Test
    void shouldGetTaskById() {
        Task task1 = new Task("name", "dsc", Status.NEW);
        task1.setId(1);
        assertEquals(task1, taskManager.getTaskById(1));
    }

    @Test
    void shouldGetSubtaskById() {
        Subtask subtask1 = new Subtask("name", "dsc", Status.NEW, 4);
        subtask1.setId(6);
        assertEquals(subtask1, taskManager.getSubtaskById(6));
    }

    @Test
    void shouldGetEpicById() {
        Epic epic1 = new Epic("name", "dsc");
        epic1.setId(4);
        assertEquals(epic1, taskManager.getEpicById(4));
    }

    @Test
    void shouldUpdTask() {
        Task task1 = new Task("new name", "new  dsc", Status.DONE);
        task1.setId(1);

        taskManager.updTask(task1);
        assertEquals(task1, taskManager.getTaskById(1));
    }

    @Test
    void shouldUpdEpic() {
        Epic epic1 = new Epic("new name", "new  dsc");
        epic1.setId(4);
        taskManager.updEpic(epic1);
        assertEquals(epic1, taskManager.getEpicById(4));
    }

    @Test
    void shouldUpdSubtask() {
        Subtask subtask1 = new Subtask("new name", " new dsc", Status.DONE, 4);
        subtask1.setId(6);
        taskManager.updSubtask(subtask1);
        assertEquals(subtask1, taskManager.getSubtaskById(6));

    }

    @Test
    void shouldRmvTaskById() {
        Task task1 = new Task("name", "dsc", Status.NEW);
        task1.setId(1);
        Task task2 = new Task("name", "dsc", Status.NEW);
        task2.setId(2);

        ArrayList<Task> list = new ArrayList<>();
        list.add(task1);
        list.add(task2);

        taskManager.rmvTaskById(3);

        assertEquals(list, taskManager.getTasks());

    }

    @Test
    void shouldRmvEpicById() {
        Epic epic1 = new Epic("name", "dsc");
        epic1.setId(4);

        ArrayList<Epic> list = new ArrayList<>();
        list.add(epic1);

        taskManager.rmvEpicById(5);

        assertEquals(list, taskManager.getEpics());
    }

    @Test
    void shouldRmvSubtaskById() {
        Subtask subtask1 = new Subtask("sbt", "dsc", Status.NEW, 4);
        subtask1.setId(6);

        ArrayList<Subtask> list = new ArrayList<>();
        list.add(subtask1);

        taskManager.rmvSubtaskById(7);

        assertEquals(list, taskManager.getSubtasks());
    }

    @Test
    void shouldGetSubtaskByEpic() {
        Subtask subtask1 = new Subtask("name", "dsc", Status.NEW, 4);
        subtask1.setId(6);
        Subtask subtask2 = new Subtask("name", "dsc", Status.NEW, 4);
        subtask2.setId(7);

        ArrayList<Subtask> list = new ArrayList<>();
        list.add(subtask1);
        list.add(subtask2);

        assertEquals(list, taskManager.getSubtaskByEpicId(4));
    }
}