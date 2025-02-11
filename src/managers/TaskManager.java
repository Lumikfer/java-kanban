package managers;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    Task getTasksById(int taskId);

    Epic getEpicsById(int epicId);

    Subtask getSubtasksById(int subtaskId);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    void removeTaskById(int taskId);

    void removeEpicById(int epicId);

    void removeSubtaskById(int subtaskId);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    List<Subtask> getSubtasksForEpic(int epicId);

    List<Task> getHistory();

    TreeSet<Task> getPrioritizedTasks();

    TreeSet<Epic> getPrioritizedEpic();
}
