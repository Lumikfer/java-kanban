package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {

    void createTask(Task task);

    void createEpicTask(Epic epic);

    void createSubTask(Subtask subtask);

    List<Task> getListTasks();

    List<Task> getListEpicTasks();

    List<Task> getListSubTasks();

    Task getTaskById(Long id);

    Subtask getSubTaskById(Long id);

    Epic getEpicTaskById(Long id);

    void updateEpicStatus(Epic epic);

    Task deleteTask(Task task);

    Epic deleteEpicTask(Epic task);

    void deleteSubTask(Subtask task);

    Task deleteTaskById(Long id);

    Epic deleteEpicTaskById(Long id);

    void deleteSubTaskById(Long id);

    List<Task> getHistory();

    void clearListTasks();

    void clearListEpicTasks();

    void clearListSubTasks(Epic epic);

    List<Task> getPrioritizedTasks();
}
