package manager;

import tasks.*;

import java.util.List;

public interface TaskManager {

    Task addTask(Task task);

    Epic addEpic(Epic epic);

    Subtask addSubtask(Subtask subtask);

    Task updateTask(Task task);

    Epic updateEpic(Epic epic);

    Subtask updateSubtask(Subtask subtask);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    List<Subtask> getEpicSubtasks(int id);

    Task getTaskByID(int id);

    Epic getEpicByID(int id);

    Subtask getSubtaskByID(int id);

    void removeTaskByID(int id);

    void removeEpicByID(int id);

    void removeSubtaskByID(int id);

    void deleteTasks();

    void deleteEpics();

    void deleteSubtasks();

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

    boolean isTaskOverlapping(Task task, int ignoreTaskId);
}
